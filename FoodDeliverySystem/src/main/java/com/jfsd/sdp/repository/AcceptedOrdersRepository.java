package com.jfsd.sdp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jfsd.sdp.model.AcceptedOrders;

import jakarta.transaction.Transactional;

@Repository
public interface AcceptedOrdersRepository extends JpaRepository<AcceptedOrders, Integer>{

	@Transactional
	@Modifying
	@Query("DELETE FROM AcceptedOrders a WHERE a.orderid = ?1")
	public void deleteorderbasedorderid(String orderid);
}
