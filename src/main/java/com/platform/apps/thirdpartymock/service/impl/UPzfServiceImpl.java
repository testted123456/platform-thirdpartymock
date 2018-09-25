package com.platform.apps.thirdpartymock.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.apps.thirdpartymock.entity.UPzf;
import com.platform.apps.thirdpartymock.repository.UPzfRepository;
import com.platform.apps.thirdpartymock.service.UPzfService;

@Service
public class UPzfServiceImpl implements UPzfService {
	
	public static Logger logger = LoggerFactory.getLogger(UPzfServiceImpl.class);

	@Autowired
	UPzfRepository upzfRepository;

	@Override
	public void add(String reqMsgId, String smzfMsgId, String callBackUrl, String totalAmount, String totalFee, Short optStatus) {
		// TODO Auto-generated method stub
		logger.info("开始新增upzf...");
		UPzf upzf = new UPzf();
		upzf.setReqMsgId(reqMsgId);
		upzf.setSmzfMsgId(smzfMsgId);
		upzf.setCallBackUrl(callBackUrl);
		upzf.setTotalAmount(totalAmount);
		upzf.setTotalFee(totalFee);
		upzf.setOptStatus((short)0);
		upzfRepository.save(upzf);
	}

	@Override
	public void update(UPzf upzf) {
		// TODO Auto-generated method stub
		logger.info("开始更新upzf...");
		upzfRepository.save(upzf);
	}

	@Override
	public UPzf findByReqMsgId(String reqMsgId) {
		// TODO Auto-generated method stub
		return upzfRepository.findByReqMsgId(reqMsgId);
	}

	@Override
	public List<UPzf> getUnHandledOrder() {
		// TODO Auto-generated method stub
		return upzfRepository.findByOptStatusNot((short)1);
	}

}
