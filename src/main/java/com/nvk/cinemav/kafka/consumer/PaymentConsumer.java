package com.nvk.cinemav.kafka.consumer;

import com.nvk.cinemav.config.VNPAYConfig;
import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.dto.PaymentRequestDTO;
import com.nvk.cinemav.dto.PaymentUrlDTO;
import com.nvk.cinemav.repository.SeatRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.repository.TicketRepository;
import com.nvk.cinemav.repository.UserRepository;
import com.nvk.cinemav.service.IPaymentService;
import com.nvk.cinemav.service.impl.PaymentService;
import com.nvk.cinemav.vnpay.VNPAYservice;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
  private static  final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
  private final KafkaTemplate<String, BookingDTO> kafkaTemplate;
  private final KafkaTemplate<String, PaymentUrlDTO> paymentUrlKafkaTemplate;
  private final VNPAYservice vnpayService;
  private final IPaymentService paymentService;

  public PaymentConsumer(KafkaTemplate<String, BookingDTO> kafkaTemplate,
      VNPAYservice vnpayService,
      KafkaTemplate<String, PaymentUrlDTO> paymentUrlKafkaTemplate,
      IPaymentService paymentService) {
    this.kafkaTemplate = kafkaTemplate;
    this.vnpayService = vnpayService;
    this.paymentUrlKafkaTemplate = paymentUrlKafkaTemplate;
    this.paymentService = paymentService;
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

  @KafkaListener(topics = "payment-url", groupId = "payment-group")
  public void updatePaymentUrl(String url, String email) {
    paymentService.setPaymentUrl(email, url);
  }

  @KafkaListener(topics = "payment-request", groupId = "payment-group")
  public void processPayment(PaymentRequestDTO paymentData) {
    log.info("📥 Received payment request: " + paymentData);

    // Gọi VNPAY để tạo đơn thanh toán
    String vnpayUrl = vnpayService.createOrder(
        paymentData.getBookingInfo().getPrice(),
        paymentData.getBookingInfo().getOrderInfo(),
        paymentData.getReturnUrl()
    );
    PaymentUrlDTO paymentUrlDTO = new PaymentUrlDTO();
    paymentUrlDTO.setUrl(vnpayUrl);
    paymentUrlDTO.setEmail(paymentData.getBookingInfo().getEmail());
    // 🟢 Gửi tiếp sự kiện "payment-url-generated" đến Kafka
    paymentUrlKafkaTemplate.send("payment-url", paymentUrlDTO);
  }
}
