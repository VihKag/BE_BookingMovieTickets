package com.nvk.cinemav.kafka.producer;

import com.nvk.cinemav.entity.Ticket;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketProducer {
  private KafkaTemplate<String, String> kafkaTemplate;
  public TicketProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }
  public void sendBookingEvent(String bookingInfo) {
    kafkaTemplate.send("booking-created", bookingInfo);
  }
}
