package com.jfsd.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jfsd.sdp.model.Restaurants;

public interface RestaurantsRepository extends JpaRepository<Restaurants, Integer>{

	@Query("select r from Restaurants r where email=?1 and password=?2")
	public Restaurants checkRestaurantLogin(String e,String p);
	
}
