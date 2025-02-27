package com.nvk.cinemav.service;

import com.nvk.cinemav.entity.Payment;
import com.nvk.cinemav.service.impl.PaymentService;

public interface IPaymentService {
  Payment saveSuccessfulPayment(Integer amount);
  public void setPaymentUrl(String email, String url);
  String getPaymentUrl(String email);
  void removePaymentUrl(String email);
}
