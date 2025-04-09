package com.nvk.cinemav.controller;

import com.nvk.cinemav.config.VNPAYConfig;
import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.kafka.producer.PaymentProducer;
import com.nvk.cinemav.service.IPaymentService;

import com.nvk.cinemav.vnpay.VNPAYservice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
  private final VNPAYservice vnpayService;
  private final IPaymentService paymentService;
  private final KafkaTemplate kafkaTemplate;
  private final PaymentProducer paymentProducer;

  public PaymentController(VNPAYservice vnpayService, IPaymentService paymentService,
      KafkaTemplate kafkaTemplate, PaymentProducer paymentProducer) {
    this.vnpayService = vnpayService;
    this.paymentService = paymentService;
    this.kafkaTemplate = kafkaTemplate;
    this.paymentProducer = paymentProducer;
  }

  @PostMapping("/create")
  public String createPayment(@RequestParam("amount") int orderTotal,
      @RequestParam("orderInfo") String orderInfo,
      HttpServletRequest request) {
    String baseUrl =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    String vnpayUrl = vnpayService.createOrder(orderTotal, orderInfo, baseUrl);
    return "redirect:" + vnpayUrl;
  }

  @GetMapping("/vnpay-return")
  public String paymentReturn(@RequestParam Map<String, String> params) {
    String vnp_SecureHash = params.remove("vnp_SecureHash");
    String generatedHash = VNPAYConfig.hashAllFields(params);

    if (generatedHash.equals(vnp_SecureHash)) {
      String status = params.get("vnp_ResponseCode");
      if ("00".equals(status)) {
        // Gửi sự kiện "payment-success" đến Kafka để tiếp tục xử lý vé
        kafkaTemplate.send("payment-success", params.get("vnp_TxnRef"));
        return "Thanh toán thành công!";
      } else {
        return "Thanh toán thất bại! Mã lỗi: " + status;
      }
    }
    return "Chữ ký không hợp lệ!";
  }

  @PostMapping("/create_payment")
  public String createPaymentProducer(@RequestBody BookingDTO bookingDTO,
      HttpServletRequest request) {
    String baseUrl =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    bookingDTO.setBaseUrl(baseUrl);
    // 🟢 Gửi sự kiện "payment-request" vào Kafka
    paymentProducer.sendPaymentRequest(bookingDTO);

    return "Yêu cầu thanh toán đã được gửi. Đang xử lý...";
  }


  @GetMapping("/get-payment-url")
  public ResponseEntity<?> getPaymentUrl(@RequestParam String email) {
    String url = paymentService.getPaymentUrl(email);
    if (url == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No payment URL found for this booking.");
    }
    paymentService.removePaymentUrl(email); // Xóa URL sau khi lấy
    return ResponseEntity.ok(url);
  }
}
