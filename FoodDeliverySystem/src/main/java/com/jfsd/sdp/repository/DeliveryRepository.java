package com.jfsd.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jfsd.sdp.model.DeliveryBoy;



@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryBoy, Integer>{
	
	@Query("select d from DeliveryBoy d where delId=?1")
	public DeliveryBoy loginvalidate(String uname);
	
	@Query("select d from DeliveryBoy d where delId=?1")
	public DeliveryBoy  updateprofile(String uname);
} 
