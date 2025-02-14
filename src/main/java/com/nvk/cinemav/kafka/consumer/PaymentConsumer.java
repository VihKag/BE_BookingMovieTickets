package com.nvk.cinemav.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
  private static  final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
  private final KafkaTemplate<String, String> kafkaTemplate;

  public PaymentConsumer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "booking-created", groupId = "payment-group")
  public void processPayment(String bookingInfo) {
    log.info("Processing payment for: " + bookingInfo);
    // Giả lập thanh toán thành công
    kafkaTemplate.send("payment-success", bookingInfo);
  }
}
