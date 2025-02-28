package com.nvk.cinemav.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nvk.cinemav.dto.BookingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketProducer {
  private static final Logger log = LoggerFactory.getLogger(TicketProducer.class);
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public TicketProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  public void sendBookingEvent(BookingDTO bookingInfo) {
    try {
      String json = objectMapper.writeValueAsString(bookingInfo);
      kafkaTemplate.send("booking-process", json);
      log.info("📤 Sent booking event to Kafka: {}", json);
    } catch (JsonProcessingException e) {
      log.error("Error processing message: ", e);
      throw new RuntimeException(e);
    }
  }
}
