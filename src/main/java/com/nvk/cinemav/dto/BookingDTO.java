package com.nvk.cinemav.dto;

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
  private LocalDateTime bookingDate;
  private Payment payment;
  private String orderInfo;
  private String email;
}
