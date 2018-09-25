package com.platform.apps.thirdpartymock.service;

import java.util.List;

import com.platform.apps.thirdpartymock.entity.UPzf;

public interface UPzfService {

	public void add(String reqMsgId, String smzfMsgId, String callBackUrl, String totalAmount, String totalFee, Short optStatus);
	
	public void update(UPzf upzf);
	
	public UPzf findByReqMsgId(String reqMsgId);
	
	public List<UPzf> getUnHandledOrder();
}
