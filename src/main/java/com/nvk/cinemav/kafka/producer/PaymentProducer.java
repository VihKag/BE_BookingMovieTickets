package com.nvk.cinemav.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.dto.PaymentRequestDTO;
import com.nvk.cinemav.dto.PaymentUrlDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {
  private final static Logger log = LoggerFactory.getLogger(PaymentProducer.class);
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, PaymentRequestDTO> kafkaTemplateObj;
  private final ObjectMapper objectMapper;
  public PaymentProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaTemplate<String, PaymentRequestDTO> kafkaTemplateObj) {
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTemplateObj = kafkaTemplateObj;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule()); // Đăng ký hỗ trợ LocalDateTime
  }

  public void sendPaymentRequest(BookingDTO bookingInfo) {
    try{
      log.info("📤 Sent payment request to Kafka: " + bookingInfo);
      String json = objectMapper.writeValueAsString(bookingInfo);
      kafkaTemplate.send("payment-request", json);
    }
    catch (JsonProcessingException e){
      e.printStackTrace();
      log.error(e.getMessage());
    }
  }

  public void sendPaymentUrl(PaymentUrlDTO paymentUrlDTO) {
    try{
      String json = objectMapper.writeValueAsString(paymentUrlDTO);
      log.info("📤 Sent payment url to Kafka: " + json);
      kafkaTemplate.send("payment-url", json);
    }catch (JsonProcessingException e){
      log.error("Error processing message: ", e);
      throw new RuntimeException(e);
    }
  }
}
