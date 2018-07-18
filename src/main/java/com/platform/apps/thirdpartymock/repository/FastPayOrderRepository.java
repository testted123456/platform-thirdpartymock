package com.platform.apps.thirdpartymock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.platform.apps.thirdpartymock.entity.FastPayOrder;

public interface FastPayOrderRepository extends JpaRepository<FastPayOrder, String> {

	FastPayOrder findBySystemNo(String systemNo);
	
	FastPayOrder findByOrderNo(String orderNo);
	
	List<FastPayOrder> findByOptStatusIn(List<Short> status);
}
