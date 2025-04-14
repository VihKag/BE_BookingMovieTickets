package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
  private int id;
  private String number;

  public SeatDTO(Seat seat) {
    this.id = seat.getId();
    this.number= seat.getSeatNumber();
  }
}
