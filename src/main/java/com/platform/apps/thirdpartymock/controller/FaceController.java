package com.platform.apps.thirdpartymock.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.alibaba.fastjson.JSONObject;
import com.platform.apps.thirdpartymock.component.ApplicationContextProvider;
import com.platform.apps.thirdpartymock.component.result.Result;
import com.platform.apps.thirdpartymock.component.result.ResultCode;
import com.platform.apps.thirdpartymock.component.result.ResultUtil;
import com.platform.apps.thirdpartymock.rule.face.FaceBackRule;
import com.platform.apps.thirdpartymock.rule.face.FaceFrontRule;
import com.platform.apps.thirdpartymock.rule.face.FaceRule;
import com.platform.apps.thirdpartymock.rule.face.FaceVerify1Rule;
import com.platform.apps.thirdpartymock.rule.face.FaceVerify2Rule;

@Controller
@RequestMapping(value="faceid")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FaceController {
	
	public static Logger logger = LoggerFactory.getLogger(FaceController.class);
	
	@Autowired
	FaceFrontRule faceFrontRule;
	
	@Autowired
	FaceBackRule faceBackRule;
	
	@Autowired
	FaceVerify1Rule faceVerify1Rule;
	
	@Autowired
	FaceVerify2Rule faceVerify2Rule;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	ApplicationContextProvider applicationContextProvider;

	@ResponseBody
	@PostMapping(value="v2/verify")
	public String verify(MultipartHttpServletRequest request){
		
		logger.info("v2");

		MultipartFile image = request.getFile("image");
		
		MultipartFile image_best = request.getFile("image_best");
		
		String customerId = null;
		
		if(image != null){//第一次处理
			logger.info("开始认证第一次...");
			
			String fileName = image.getOriginalFilename();
			
			logger.info("v2,{}", fileName);
			String [] fns = fileName.split("-");
			customerId = fns[0];
			logger.info("customerId:, {}", customerId);
			
//			stringRedisTemplate.opsForValue().set(customerId, "verify", 60*2, TimeUnit.SECONDS);
			
			JSONObject reqJson = new JSONObject();
			JSONObject resJson = faceVerify1Rule.getRuleResponse(reqJson);
			String response = resJson.toJSONString();
			logger.info("第一次认证结果: {}", response);
			return response;
		}else if(null != image_best){//处理背面照
			logger.info("开始认证第二次...");
			String fileName = image_best.getOriginalFilename();
			
			logger.info("v2,{}", fileName);
			String [] fns = fileName.split("-");
			customerId = fns[0];
			logger.info("customerId:, {}", customerId);
			
//			stringRedisTemplate.delete(customerId);
			
			JSONObject reqJson = new JSONObject();
			JSONObject resJson = faceVerify2Rule.getRuleResponse(reqJson);
			String response = resJson.toJSONString();
			logger.info("第二次认证结果: {}", response);
			return response;
		}
		
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value="v1/ocridcard")
	public String ocridcard(MultipartHttpServletRequest request){
		logger.info("v1");

		MultipartFile file = request.getFile("image");
		String fileName = file.getOriginalFilename();
		
		logger.info("v1,{}", fileName);
		
		String customerId = null;
		
		if(null != fileName){
			String [] fns = fileName.split("-");
			customerId = fns[0];
			logger.info("customerId:, {}", customerId);
		}
		
		boolean isCustomerIdExisted = stringRedisTemplate.hasKey(customerId);
		
		if(isCustomerIdExisted == false){//处理正面照
			logger.info("开始认证正面照");
			stringRedisTemplate.opsForValue().set(customerId, "ocridcard", 60*2, TimeUnit.SECONDS);
			
			JSONObject reqJson = new JSONObject();
			JSONObject resJson = faceFrontRule.getRuleResponse(reqJson);
			String response = resJson.toJSONString();
			logger.info("正面照认证结果: {}", response);
			return response;
		}else {//处理背面照
			logger.info("开始认证背面照");
			stringRedisTemplate.delete(customerId);
			
			JSONObject reqJson = new JSONObject();
			JSONObject resJson = faceBackRule.getRuleResponse(reqJson);
			String response = resJson.toJSONString();
			logger.info("背面照认证结果: {}", response);
			return response;
		}
	}
	
	@RequestMapping(value="setResponse/{name}")
	@ResponseBody
	public Result setResponse(@PathVariable String name, @RequestBody Map<String, String> responseMap) {
		String response = responseMap.get("response");
		logger.info("开始保存快捷支付接口：{}", response);
		FaceRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.face.FaceRule.class);
		rule.setResponse(name, response);
		return ResultUtil.success();
	}
	
	@ResponseBody
	@GetMapping(value="resetResponse/{name}")
	public Result reset(@PathVariable String name){
		logger.info("begin to resetResponse response...");
		
		FaceRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.face.FaceRule.class);

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
		FaceRule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.face.FaceRule.class);
		
		Map<String, String> map = rule.getInfos();
		
		return ResultUtil.success(map);
	}
}
