package com.platform.apps.thirdpartymock.service.impl;

import java.util.ArrayList;
import java.util.List;

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
	public void add(String systemNo, String orderNo, String cardNo, Short optStatus) {
		// TODO Auto-generated method stub
		logger.info("开始新增快捷支付订单，systemNo:{}, orderNo:{}", systemNo, orderNo);
		FastPayOrder fastPayOrder = new FastPayOrder();
		fastPayOrder.setOrderNo(orderNo);
		fastPayOrder.setSystemNo(systemNo);
		fastPayOrder.setCardNo(cardNo);
		fastPayOrder.setOptStatus(optStatus);
		fastPayOrderRepository.save(fastPayOrder);
	}

	@Override
	public FastPayOrder getBySystemNo(String systemNo) {
		// TODO Auto-generated method stub
		logger.info("开始查询快捷支付订单，systemNo:{}", systemNo);
		FastPayOrder fastPayOrder = fastPayOrderRepository.findBySystemNo(systemNo);
		return fastPayOrder;
	}

	@Override
	public void updateStatus(String systemNo, Short optStatus) {
		// TODO Auto-generated method stub
		FastPayOrder fastPayOrder = fastPayOrderRepository.findBySystemNo(systemNo);
		fastPayOrder.setOptStatus(optStatus);
		fastPayOrderRepository.save(fastPayOrder);
	}

	@Override
	public List<FastPayOrder> getFastPayCallBack() {
		// TODO Auto-generated method stub
		List<Short> status = new ArrayList<>();
		status.add((short)1);
		status.add((short)2);
		List<FastPayOrder> list = fastPayOrderRepository.findByOptStatusIn(status);
		return list;
	}

}
