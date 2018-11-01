package com.platform.apps.thirdpartymock.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.platform.apps.thirdpartymock.component.ApplicationContextProvider;
import com.platform.apps.thirdpartymock.component.result.Result;
import com.platform.apps.thirdpartymock.component.result.ResultCode;
import com.platform.apps.thirdpartymock.component.result.ResultUtil;
import com.platform.apps.thirdpartymock.rule.jz.JZAuthRule;
import com.platform.apps.thirdpartymock.rule.jz.JZIDAuthRule;
import com.platform.apps.thirdpartymock.rule.upzf.UPZFRule;

@Controller
@RequestMapping(value="jz")
@CrossOrigin(origins = "*", maxAge = 3600)
public class JZController {
	
	public static Logger logger = LoggerFactory.getLogger(JZController.class);
	
	@Autowired
	JZIDAuthRule jzIDAuthRule;
	
	@Autowired
	ApplicationContextProvider applicationContextProvider;

	@ResponseBody
	@GetMapping(value="auth")
	public String auth(@RequestParam String sign, String pid, String passName, @RequestParam(name="Hashcode") String hasCode){
		logger.info("验证身份证，姓名：{}，身份证号：{}", passName, pid);
		
		/*String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<PSG_certIDverify xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://xiaoher.jzdata.com/\">"  
			+ "<ErrorRes>"    
			+ "<Err_code>200</Err_code>"    
			+ "<Err_content>ERROR:未匹配到对应身份证姓名数据</Err_content>"  
			+ "</ErrorRes>"
			+ "</PSG_certIDverify>";*/
		
		String result = jzIDAuthRule.getInfos().get("response");
		return result;
	}
	
	@RequestMapping(value="setResponse/{name}")
	@ResponseBody
	public Result setResponse(@PathVariable String name, @RequestBody Map<String, String> responseMap) {
		String response = responseMap.get("response");
		logger.info("开始保存快捷支付接口：{}", response);
		JZAuthRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.jz.JZAuthRule.class);
		rule.setResponse(name, response);
		return ResultUtil.success();
	}
	
	@ResponseBody
	@GetMapping(value="resetResponse/{name}")
	public Result reset(@PathVariable String name){
		logger.info("begin to resetResponse response...");
		
		JZAuthRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.jz.JZAuthRule.class);

		
//		boolean result = jzIDAuthRule.setDefault("jz");
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
		JZAuthRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.jz.JZAuthRule.class);

		Map<String, String> map = rule.getInfos();
		
		return ResultUtil.success(map);
	}
}
