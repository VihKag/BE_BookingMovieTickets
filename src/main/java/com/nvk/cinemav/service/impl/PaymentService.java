package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.entity.Payment;
import com.nvk.cinemav.repository.PaymentRepository;
import com.nvk.cinemav.service.IPaymentService;
import com.nvk.cinemav.type.PaymentStatus;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
  private final PaymentRepository paymentRepository;
  private final Map<String, String> paymentUrls = new ConcurrentHashMap<>();

  public PaymentService(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }
  @Override
  public void setPaymentUrl(String email, String url) {
    paymentUrls.put(email, url);
  }
  @Override
  public String getPaymentUrl(String email) {
    return paymentUrls.get(email);
  }
  @Override
  public void removePaymentUrl(String email) {
    paymentUrls.remove(email);
  }
  
  @Override
  public Payment saveSuccessfulPayment(Integer amount) {
    Payment payment = new Payment();
    payment.setAmount(amount);
    payment.setDate(LocalDateTime.now());
    payment.setStatus(PaymentStatus.SUCCESS);
    return paymentRepository.save(payment);
  }

}
