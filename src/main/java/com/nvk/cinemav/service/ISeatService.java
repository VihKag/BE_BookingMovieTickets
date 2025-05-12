package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.SeatStatusDTO;
import com.nvk.cinemav.entity.Seat;
import java.util.List;
import java.util.UUID;

public interface ISeatService {
  Seat getSeatById(Integer id);             // Fetch a seat by ID

  Seat createSeat(Seat seat);            // Create a new seat

  Seat updateSeat(Integer id, Seat seat);   // Update a seat

  void deleteSeat(Integer id);              // Delete a seat
  void  updateSeatStatusAfterShow();
  List<Seat> getAvailableSeats(List<Integer> seatIds);
  List<Seat> getAvailableSeatsInShow(UUID showId);
  List<SeatStatusDTO> getSeatStatusByScreenId(Integer screenId);
  
  // New methods
  void unlockSeats(List<Integer> seatIds);
  List<SeatStatusDTO> getSeatStatusByShowId(UUID showId);
}
