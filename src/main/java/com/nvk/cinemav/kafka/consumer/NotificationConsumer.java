package com.nvk.cinemav.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;

public class NotificationConsumer {

  @KafkaListener(topics = "payment-success", groupId = "notification-group")
  public void sendEmailNotification(String bookingId) {
    System.out.println("📩 Sending email for booking: " + bookingId);
    // Gửi email xác nhận cho khách hàng
  }

}
