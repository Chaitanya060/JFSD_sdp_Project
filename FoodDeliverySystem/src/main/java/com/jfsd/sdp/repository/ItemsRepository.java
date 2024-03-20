package com.jfsd.sdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jfsd.sdp.model.Items;



@Repository
public interface ItemsRepository extends CrudRepository<Items, Integer>
{
	@Query("from Items p where p.category=?1")
	public List<Items> viewAllProductsByCategory(String category);
}
