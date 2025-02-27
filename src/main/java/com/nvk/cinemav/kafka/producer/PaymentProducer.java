package com.nvk.cinemav.kafka.producer;

import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.dto.PaymentRequestDTO;
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
  public PaymentProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaTemplate<String, PaymentRequestDTO> kafkaTemplateObj) {
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTemplateObj = kafkaTemplateObj;
  }

  public void sendPaymentRequest(String baseUrl, BookingDTO bookingInfo) {
    PaymentRequestDTO paymentRequest = new PaymentRequestDTO(baseUrl, bookingInfo);
    log.info("📤 Sent payment request to Kafka: " + paymentRequest);
    kafkaTemplateObj.send("payment-request", paymentRequest);
  }

  public void sendPaymentUrl(String url) {
    log.info("📤 Sent payment url to Kafka: " + url);
    kafkaTemplate.send("payment-url", url);
  }
}
