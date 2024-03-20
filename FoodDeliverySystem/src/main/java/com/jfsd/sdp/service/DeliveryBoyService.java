package com.jfsd.sdp.service;

import com.jfsd.sdp.model.DeliveryBoy;

public interface DeliveryBoyService {
  public String addUser(DeliveryBoy e);
  public DeliveryBoy loginvalidate(String uname);
  public String updateprofile(DeliveryBoy c);
}