package com.platform.apps.thirdpartymock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.platform.apps.thirdpartymock.component.result.ResultUtil;
import com.platform.apps.thirdpartymock.entity.FastPayOrder;
import com.platform.apps.thirdpartymock.rule.CallBackRule;
import com.platform.apps.thirdpartymock.rule.Rule;
import com.platform.apps.thirdpartymock.service.FastPayOrderService;

@RequestMapping(value="fastPay")
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class FastPayController {

	public static Logger logger = LoggerFactory.getLogger(FastPayController.class);
	
	@Autowired
	ApplicationContextProvider applicationContextProvider;
	
	@Autowired
	FastPayOrderService fastPayOrderService;
	
	@GetMapping(value="getInfos")
	@ResponseBody
	public Result getInfos(@RequestParam String name) {
		logger.info("开始查询快捷支付接口：{}", name);
		Rule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.Rule.class);

		Map<String, String> map = rule.getInfos();
		
		return ResultUtil.success(map);
	}
	
	@RequestMapping(value="setResponse/{name}")
	@ResponseBody
	public Result setResponse(@PathVariable String name, @RequestBody Map<String, String> responseMap) {
		String response = responseMap.get("response");
		logger.info("开始保存快捷支付接口：{}", response);
		Rule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.Rule.class);
		rule.setResponse(name, response);
		return ResultUtil.success();
	}
	
	@RequestMapping(value="resetResponse")
	@ResponseBody
	public Result resetResponse(@RequestParam String name) {
		logger.info("开始重置快捷支付接口：{}", name);
		Rule rule = applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.Rule.class);
		rule.setDefault(name);
		return ResultUtil.success();
	}
	
	@RequestMapping(value="getCallBack")
	@ResponseBody
	public Result getPayCallBack(@RequestParam String name) {
		logger.info("开始获取需要回调的支付订单");
		
		CallBackRule rule =  applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.CallBackRule.class);
		
		Map<String, String> map = rule.getInfos();
		
		if("fastPayCallBack".equals(name)) {
			List<FastPayOrder> list = fastPayOrderService.getFastPayCallBack();
//			Map<String, Object> result = new HashMap<>();
			
//			List<String> cardNos = 
//					list.stream().map(x->{return x.getCardNo();}).collect(Collectors.toList());
			Map<String,List<FastPayOrder>> cardNos =
			list.stream().collect(Collectors.groupingBy(FastPayOrder::getCardNo));
			
			/*Map<String, List<String>> tmp = new HashMap<>();
			
			cardNos.forEach((k,v)->{
				tmp.put(k, v.stream().map(FastPayOrder::getOrderNo).collect(Collectors.toList()));
			});
			
			result.put("orders", tmp);
			result.put("request", map.get("request"));
			result.put("defaultRequest", map.get("defaultRequest"));*/
//			map.put("cardNos", cardNos.toString());
			return ResultUtil.success(cardNos);
		}
		
		
		return ResultUtil.success(map);
	}
	
	@RequestMapping(value="callBack")
	@ResponseBody
	public Result callBack( @RequestBody Map<String, String> requestMap) {
		String name = requestMap.get("name").toString();
		CallBackRule rule =  applicationContextProvider.getBean(name + "Rule", com.platform.apps.thirdpartymock.rule.CallBackRule.class);
		
		boolean result = rule.callBack(requestMap);
		return ResultUtil.success();
	}
	
	@RequestMapping(value="getPayOrder")
	@ResponseBody
	public Result getPayOrder(@RequestParam String orderNo) {
		FastPayOrder fastPayOrder =	fastPayOrderService.getByOrderNo(orderNo);
		return ResultUtil.success(fastPayOrder);
	}

}
