package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.SeatDTO;
import com.nvk.cinemav.dto.SeatStatusDTO;
import com.nvk.cinemav.entity.Seat;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.repository.SeatRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.service.ISeatService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService implements ISeatService {

  private final SeatRepository seatRepository;
  private final ShowRepository showRepository;

  @Override
  public List<Seat> getAvailableSeatsInShow(UUID showId) {
    return seatRepository.findAvailableSeatsByShowId(showId);
  }

  @Override
  public Seat getSeatById(Integer id) {
    return seatRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Seat not found"));
  }

  @Override
  public Seat createSeat(Seat seat) {
    return seatRepository.save(seat);
  }

  @Override
  public Seat updateSeat(Integer id, Seat seat) {
    Seat existingSeat = getSeatById(id);
    existingSeat.setSeatNumber(seat.getSeatNumber());
    existingSeat.setType(seat.getType());
    existingSeat.setScreen(seat.getScreen());
    return seatRepository.save(existingSeat);
  }

  @Override
  public void deleteSeat(Integer id) {
    seatRepository.deleteById(id);
  }

  @Scheduled(fixedRate = 60000) // Run every minute
  @Transactional
  @Override
  public void updateSeatStatusAfterShow() {
    LocalDateTime now = LocalDateTime.now();
    List<Show> finishedShows = showRepository.findFinishedShows(now);

    for (Show show : finishedShows) {
      log.info("🔄 Updating seat status for show: {}", show.getId());
      seatRepository.updateSeatStatusByShow(show.getId(), true);
    }
  }

  @Override
  public List<Seat> getAvailableSeats(List<Integer> seatIds) {
    return seatRepository.findAllById(seatIds).stream()
        .filter(seat -> !seat.getBookedSeats().isEmpty())
        .collect(Collectors.toList());
  }

  @Override
  public List<SeatStatusDTO> getSeatStatusByScreenId(Integer screenId) {
    List<Seat> seats = seatRepository.findSeatsByScreenId(screenId);
    return seats.stream()
        .map(seat -> new SeatStatusDTO(seat.getId(), seat.getSeatNumber(), 
            !seat.getBookedSeats().isEmpty()))
        .collect(Collectors.toList());
  }

  @Override
  public void unlockSeats(List<Integer> seatIds) {
    seatRepository.unlockSeats(seatIds);
  }

  @Override
  public List<SeatStatusDTO> getSeatStatusByShowId(UUID showId) {
    List<Seat> seats = seatRepository.findSeatsByShowId(showId);
    return seats.stream()
        .map(seat -> new SeatStatusDTO(seat.getId(), seat.getSeatNumber(), 
            !seat.getBookedSeats().isEmpty()))
        .collect(Collectors.toList());
  }
}
