package com.nvk.cinemav.service;

import com.nvk.cinemav.entity.Seat;
import java.util.List;
import java.util.UUID;

public interface ISeatService {
  void  updateSeatStatusAfterShow();
  List<Seat> getAvailableSeats(List<Integer> seatIds);
  List<Seat> getAvailableSeatsInShow(UUID showId);
}
