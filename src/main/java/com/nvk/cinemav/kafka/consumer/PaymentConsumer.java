package com.nvk.cinemav.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nvk.cinemav.config.VNPAYConfig;
import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.dto.PaymentRequestDTO;
import com.nvk.cinemav.dto.PaymentUrlDTO;
import com.nvk.cinemav.kafka.producer.PaymentProducer;
import com.nvk.cinemav.service.IPaymentService;
import com.nvk.cinemav.vnpay.VNPAYservice;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

  private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, BookingDTO> kafkaTemplate;
  private final KafkaTemplate<String, PaymentUrlDTO> paymentUrlKafkaTemplate;
  private final VNPAYservice vnpayService;
  private final IPaymentService paymentService;
  private final PaymentProducer paymentProducer;
  private final SimpMessagingTemplate messagingTemplate;

  public PaymentConsumer(KafkaTemplate<String, BookingDTO> kafkaTemplate,
      VNPAYservice vnpayService,
      KafkaTemplate<String, PaymentUrlDTO> paymentUrlKafkaTemplate,
      IPaymentService paymentService, PaymentProducer paymentProducer,
      SimpMessagingTemplate messagingTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    this.vnpayService = vnpayService;
    this.paymentUrlKafkaTemplate = paymentUrlKafkaTemplate;
    this.paymentService = paymentService;
    this.paymentProducer = paymentProducer;
    this.messagingTemplate = messagingTemplate;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule()); // Đăng ký JavaTimeModule
  }


  @KafkaListener(topics = "payment-result", groupId = "payment-group")
  public void processPaymentResult(Map<String, String> params, BookingDTO bookingInfo) {
    String vnp_SecureHash = params.remove("vnp_SecureHash");
    String generatedHash = VNPAYConfig.hashAllFields(params);

    if (generatedHash.equals(vnp_SecureHash)) {
      String status = params.get("vnp_ResponseCode");
      if ("00".equals(status)) {
        // 🟢 Gửi tiếp sự kiện "payment-success" để xử lý vé
        kafkaTemplate.send("payment-success", bookingInfo);
        System.out.println("✅ Thanh toán thành công!");
      } else {
        System.out.println("❌ Thanh toán thất bại! Mã lỗi: " + status);
      }
    } else {
      System.out.println("⚠️ Chữ ký không hợp lệ!");
    }
  }

  @KafkaListener(topics = "payment-url", groupId = "payment-group", containerFactory = "kafkaListenerContainerFactory")
  public void updatePaymentUrl(String message) {
    try {
      log.info("📥 Received payment URL message: {}", message);

      PaymentUrlDTO paymentUrlDTO = objectMapper.convertValue(message, PaymentUrlDTO.class);
      log.info("📤 Sending payment URL to WebSocket: " + paymentUrlDTO.getUrl());
      log.info("📤 Sending payment email to WebSocket: " + paymentUrlDTO.getEmail());
      // 🟢 Gửi URL thanh toán tới frontend qua WebSocket
      messagingTemplate.convertAndSend(
          "/topic/payment-url/" + paymentUrlDTO.getEmail(),
          paymentUrlDTO
      );
    } catch (IllegalArgumentException e) {  // Lỗi khi ObjectMapper convertValue thất bại
      log.error("❌ Invalid argument when converting message: " + e.getMessage(), e);
    } catch (Exception e) {  // Bắt tất cả lỗi khác
      log.error("❌ Unexpected error processing message: " + e.getMessage(), e);
    }
  }

  @KafkaListener(topics = "payment-request", groupId = "payment-group", containerFactory = "kafkaListenerContainerFactory")
  public void processPayment(String message) {

    try {
      log.info("📥 Received payment request: {}", message);
      BookingDTO paymentData = objectMapper.readValue(message, BookingDTO.class);

      log.info("📥 Received payment request: " + paymentData);
      // Gọi VNPAY để tạo đơn thanh toán
      String vnpayUrl = vnpayService.createOrder(
          paymentData.getTotalPrice(),
          paymentData.getOrderInfo(),
          paymentData.getBaseUrl()
      );
      PaymentUrlDTO paymentUrlDTO = new PaymentUrlDTO();
      paymentUrlDTO.setUrl(vnpayUrl);
      paymentUrlDTO.setEmail(paymentData.getEmail());

      paymentProducer.sendPaymentUrl(paymentUrlDTO);
      // 🟢 Gửi tiếp sự kiện "payment-url-generated" đến Kafka
//    paymentUrlKafkaTemplate.send("payment-url", paymentUrlDTO);
    } catch (JsonProcessingException e) {
      log.info(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
