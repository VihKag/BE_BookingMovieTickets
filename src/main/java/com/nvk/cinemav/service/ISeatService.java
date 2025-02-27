package com.nvk.cinemav.service;

import com.nvk.cinemav.entity.Seat;
import java.util.List;

public interface ISeatService {
  void  updateSeatStatusAfterShow();
  List<Seat> getAvailableSeats(List<Integer> seatIds);
}
