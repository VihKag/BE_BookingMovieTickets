package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Payment;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {
  private UUID userId;
  private UUID showId;
  private Integer seatId;
  private Float price;
  private LocalDateTime bookingDate;
  private Payment payment;
}
