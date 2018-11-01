package com.platform.apps.thirdpartymock.controller;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.platform.apps.thirdpartymock.component.ApplicationContextProvider;
import com.platform.apps.thirdpartymock.component.result.Result;
import com.platform.apps.thirdpartymock.component.result.ResultCode;
import com.platform.apps.thirdpartymock.component.result.ResultUtil;
import com.platform.apps.thirdpartymock.rule.ryxauth.CardAuthRule;
import com.platform.apps.thirdpartymock.service.RyxAuthService;
import com.platform.apps.thirdpartymock.util.RyxAuthUtil;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

@RequestMapping(value="authencrypt")
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class RyxAuthController {
	
	public static Logger logger = LoggerFactory.getLogger(RyxAuthController.class);

	@Autowired
	RyxAuthService ryxAuthService;
	
	@Autowired
	ApplicationContextProvider applicationContextProvider;

	@PostMapping(value="0010")
	@ResponseBody
	public void cardAuth(HttpServletRequest request, HttpServletResponse response){
		logger.info("开始银行卡鉴权...");
		
		try {
			String privateKey = SMCryptUtil.smzfPriKey;
			String publicKey = SMCryptUtil.smzfPubKey;
			
			String strData = ryxAuthService.getResponseData();
			
			byte[] data = RyxAuthUtil.encode(strData, "QTUO0002", "UTF-8", privateKey, publicKey);
			response.getOutputStream().write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostMapping(value="0030")
	@ResponseBody
	public void idAuth(@RequestBody byte[] bytes, HttpServletRequest request, HttpServletResponse response){
		logger.info("开始身份证鉴权...");
	}
	
	@RequestMapping(value="setResponse/{name}")
	@ResponseBody
	public Result setResponse(@PathVariable String name, @RequestBody Map<String, String> responseMap) {
		String response = responseMap.get("response");
		logger.info("开始保存快捷支付接口：{}", response);
		CardAuthRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.ryxauth.CardAuthRule.class);
		rule.setResponse(name, response);
		return ResultUtil.success();
	}
	
	@ResponseBody
	@GetMapping(value="resetResponse/{name}")
	public Result reset(@PathVariable String name){
		logger.info("begin to resetResponse response...");
		
		CardAuthRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.ryxauth.CardAuthRule.class);
		boolean result = rule.setDefault(name);
		
		if(result == true){
			return ResultUtil.success();
		}else{
			return ResultUtil.error(ResultCode.UNKOWN_ERROR.getCode(), "重置响应识别");
		}
	}
	
	@GetMapping(value="getInfos")
	@ResponseBody
	public Result getInfos(@RequestParam String name) {
		logger.info("开始查询快捷支付接口：{}", name);
		CardAuthRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.ryxauth.CardAuthRule.class);

		Map<String, String> map = rule.getInfos();
		
		return ResultUtil.success(map);
	}
}
