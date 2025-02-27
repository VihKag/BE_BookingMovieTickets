package com.nvk.cinemav.kafka.producer;

import com.nvk.cinemav.dto.BookingDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketProducer {
  private KafkaTemplate<String, BookingDTO> kafkaTemplate;
  public TicketProducer(KafkaTemplate<String, BookingDTO> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }
  public void sendBookingEvent(BookingDTO bookingInfo) {
    kafkaTemplate.send("booking-process", bookingInfo);
  }
}
