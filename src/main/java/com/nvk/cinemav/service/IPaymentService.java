package com.nvk.cinemav.service;

import com.nvk.cinemav.entity.Payment;
import com.nvk.cinemav.service.impl.PaymentService;

public interface IPaymentService {
  Payment saveSuccessfulPayment(Float amount);
}
