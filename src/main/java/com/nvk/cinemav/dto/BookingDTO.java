package com.nvk.cinemav.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nvk.cinemav.entity.Payment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
  private UUID userId;
  private UUID showId;
  private List<Integer> seatId;
  private Integer price;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Định dạng ISO-8601
  private LocalDateTime bookingDate;
  private Integer totalPrice;
  private String baseUrl;
  private String orderInfo;
  private String email;
}
