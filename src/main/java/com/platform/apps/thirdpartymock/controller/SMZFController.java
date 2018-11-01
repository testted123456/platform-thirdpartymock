package com.platform.apps.thirdpartymock.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.platform.apps.thirdpartymock.component.ApplicationContextProvider;
import com.platform.apps.thirdpartymock.component.result.Result;
import com.platform.apps.thirdpartymock.component.result.ResultCode;
import com.platform.apps.thirdpartymock.component.result.ResultUtil;
import com.platform.apps.thirdpartymock.entity.UPzf;
import com.platform.apps.thirdpartymock.rule.Rule;
import com.platform.apps.thirdpartymock.rule.upzf.UPZFRule;
import com.platform.apps.thirdpartymock.service.UPzfService;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

/**
 * 扫码支付
 * @author xinxiang
 *
 */
@RequestMapping(value="ydzf")
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class SMZFController {

	public static Logger logger = LoggerFactory.getLogger(SMZFController.class);
	
	@Autowired
	UPzfService upzfService;
	
	@Autowired
	ApplicationContextProvider applicationContextProvider;
	
	@GetMapping(value="getInfos")
	@ResponseBody
	public Result getInfos(@RequestParam String name) {
		logger.info("开始查询快捷支付接口：{}", name);
		UPZFRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.upzf.UPZFRule.class);

		Map<String, String> map = rule.getInfos();
		
		return ResultUtil.success(map);
	}
	
	@ResponseBody
	@PostMapping(value="/ydzf-smdf")
	public String smzf(@RequestParam String encryptData,
			@RequestParam String encryptKey,
			@RequestParam String cooperator,
			@RequestParam String signData,
			@RequestParam String tranCode,
			@RequestParam String callBack,
			@RequestParam String reqMsgId){
		
		logger.info("扫码支付请求字段：");
		logger.info("encryptData:" + encryptData);
		logger.info("encryptKey:" + encryptKey);
		logger.info("cooperator:" + cooperator);
		logger.info("signData:" + signData);
		logger.info("tranCode:" + tranCode);
		logger.info("callBack:" + callBack);
		logger.info("reqMsgId:" + reqMsgId);
		
		String decryptData = SMCryptUtil.decryptData(encryptData, encryptKey);
		
		UPZFRule upzfRule = applicationContextProvider.getBean(tranCode + "Rule", com.platform.apps.thirdpartymock.rule.upzf.UPZFRule.class);

		JSONObject reqJson = new JSONObject();
		reqJson.put("encryptKey", encryptKey);
		reqJson.put("encryptData", JSON.parseObject(decryptData));
		reqJson.put("cooperator", cooperator);
		reqJson.put("signData", signData);
		reqJson.put("tranCode", tranCode);
		reqJson.put("callBack", callBack);
		reqJson.put("reqMsgId", reqMsgId);
		JSONObject response = upzfRule.getRuleResponse(reqJson);
		
		return response.toJSONString();
	}
	
	@RequestMapping(value="getUnhandledOrder")
	@ResponseBody
	public Result getUnhandledOrder(){
		logger.info("开始查找未处理订单...");
		List<UPzf> upzfs = upzfService.getUnHandledOrder();
		List<String> reqMsgIds = upzfs.stream().map(x->x.getReqMsgId()).collect(Collectors.toList());
		return ResultUtil.success(reqMsgIds);
	}
	
	@RequestMapping(value="setResponse/{name}")
	@ResponseBody
	public Result setResponse(@PathVariable String name, @RequestBody Map<String, String> responseMap) {
		String response = responseMap.get("response");
		logger.info("开始保存快捷支付接口：{}", response);
		UPZFRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.upzf.UPZFRule.class);
		rule.setResponse(name, response);
		return ResultUtil.success();
	}
	
	@RequestMapping(value="resetResponse/{name}")
	@ResponseBody
	public Result resetResponse(@PathVariable String name) {
		logger.info("开始重置快捷支付接口：{}", name);
		UPZFRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.upzf.UPZFRule.class);
		rule.setDefault(name);
		return ResultUtil.success();
	}
	
	@ResponseBody
	@GetMapping(value="/callback/{tranCode}/{reqMsgId}")
	public Result callBack(@PathVariable String tranCode, @PathVariable String reqMsgId){
		UPZFRule upzfRule = applicationContextProvider.getBean(tranCode + "Rule", com.platform.apps.thirdpartymock.rule.upzf.UPZFRule.class);

		JSONObject reqJson = new JSONObject();
		reqJson.put("reqMsgId", reqMsgId);
		JSONObject request = upzfRule.getRuleResponse(reqJson);
		
		Map<String, String> map = new HashMap<>();
		request.forEach((x,y)->{
			map.put(String.valueOf(x), String.valueOf(y));
		});
		
		HttpClient httpClient = new HttpClient();
 		CloseableHttpClient client = httpClient.getHttpClient();
 		CloseableHttpResponse response = null;
 		
 		String callBackUrl = upzfService.findByReqMsgId(reqMsgId).getCallBackUrl();
 		
 		try {
			response = httpClient.doPostSendForm(client, null, callBackUrl, map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 		String res = null;
 		try {
 			res = httpClient.getResBody(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 		if(null != res ){
 			return ResultUtil.success();
 		}else{
 			return ResultUtil.error(ResultCode.UNKOWN_ERROR.getCode(), "call back failed...");
 		}
			
		
	}
	
}
