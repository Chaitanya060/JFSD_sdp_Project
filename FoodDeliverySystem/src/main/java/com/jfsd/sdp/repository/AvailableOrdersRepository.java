package com.jfsd.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jfsd.sdp.model.AvailableOrders;


@Repository
public interface AvailableOrdersRepository extends JpaRepository<AvailableOrders, Integer>
{
	
}
