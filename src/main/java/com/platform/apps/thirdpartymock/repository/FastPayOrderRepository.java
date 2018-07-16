package com.platform.apps.thirdpartymock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.platform.apps.thirdpartymock.entity.FastPayOrder;

public interface FastPayOrderRepository extends JpaRepository<FastPayOrder, String> {

	FastPayOrder findBySystemNo(String systemNo);
}
