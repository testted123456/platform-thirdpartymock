package com.platform.apps.thirdpartymock.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.platform.apps.thirdpartymock.entity.UPzf;

public interface UPzfRepository extends JpaRepository<UPzf, Integer> {
	
	public UPzf findByReqMsgId(String reqMsgId);
	
	List<UPzf> findByOptStatusNot(Short optStatus);

}
