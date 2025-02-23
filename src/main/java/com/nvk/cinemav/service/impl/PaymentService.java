package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.entity.Payment;
import com.nvk.cinemav.repository.PaymentRepository;
import com.nvk.cinemav.service.IPaymentService;
import com.nvk.cinemav.type.PaymentStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
  private final PaymentRepository paymentRepository;

  public PaymentService(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }
  @Override
  public Payment saveSuccessfulPayment(Float amount) {
    Payment payment = new Payment();
    payment.setAmount(amount);
    payment.setDate(LocalDateTime.now());
    payment.setStatus(PaymentStatus.SUCCESS);
    return paymentRepository.save(payment);
  }
}
