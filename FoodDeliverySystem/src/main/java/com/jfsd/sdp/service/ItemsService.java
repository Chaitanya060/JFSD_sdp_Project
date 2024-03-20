package com.jfsd.sdp.service;

import java.util.List;

import com.jfsd.sdp.model.*;

public interface ItemsService 
{
   public String AddProduct(Items items);
   public List<Items> ViewAllProducts();
   public Items ViewProductByID(int productid);
   public void DeleteProductByID(int productid);
   List<Items> viewAllProductsByCategory(String category);
}
