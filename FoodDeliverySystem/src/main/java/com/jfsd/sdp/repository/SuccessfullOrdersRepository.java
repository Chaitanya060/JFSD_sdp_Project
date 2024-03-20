package com.jfsd.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jfsd.sdp.model.SuccessfullOrders;


@Repository
public interface SuccessfullOrdersRepository extends JpaRepository<SuccessfullOrders, Integer>{

}
