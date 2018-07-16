package com.platform.apps.thirdpartymock.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.platform.apps.thirdpartymock.entity.FastPayOrder;
import com.platform.apps.thirdpartymock.repository.FastPayOrderRepository;
import com.platform.apps.thirdpartymock.service.FastPayOrderService;

@Service
public class FastPayOrderServiceImpl implements FastPayOrderService{
	
	public static Logger logger = LoggerFactory.getLogger(FastPayOrderServiceImpl.class);
	
	@Autowired
	FastPayOrderRepository fastPayOrderRepository;

	@Override
	public void add(String systemNo, String orderNo) {
		// TODO Auto-generated method stub
		logger.info("开始新增快捷支付订单，systemNo:{}, orderNo:{}", systemNo, orderNo);
		FastPayOrder fastPayOrder = new FastPayOrder();
		fastPayOrder.setOrderNo(orderNo);
		fastPayOrder.setSystemNo(systemNo);
		fastPayOrderRepository.save(fastPayOrder);
	}

	@Override
	public FastPayOrder getBySystemNo(String systemNo) {
		// TODO Auto-generated method stub
		logger.info("开始查询快捷支付订单，systemNo:{}", systemNo);
		FastPayOrder fastPayOrder = fastPayOrderRepository.findBySystemNo(systemNo);
		return fastPayOrder;
	}

}
