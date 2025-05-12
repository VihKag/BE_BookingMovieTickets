package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.SeatDTO;
import com.nvk.cinemav.dto.SeatStatusDTO;
import com.nvk.cinemav.entity.Seat;
import com.nvk.cinemav.repository.SeatRepository;
import com.nvk.cinemav.response.ApiResponse;
import com.nvk.cinemav.service.ISeatService;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
public class SeatController {

  private final SeatRepository seatRepository;
  private final ISeatService seatService;

  public SeatController(SeatRepository seatRepository, ISeatService seatService) {
    this.seatRepository = seatRepository;
    this.seatService = seatService;
  }

  @GetMapping("/show/{showId}")
  public ResponseEntity<?> getSeatsByShow(@PathVariable UUID showId) {
    try {
      List<Seat> seats = seatService.getAvailableSeatsInShow(showId);
      return ResponseEntity.ok(new ApiResponse<>("Seats retrieved successfully", seats));
    } catch (IllegalArgumentException | DataAccessException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters"));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve seats"));
    }
  }

  @GetMapping("/available/{showId}")
  public ResponseEntity<?> getAvailableSeats(@PathVariable UUID showId) {
    try {
      List<Seat> seats = seatService.getAvailableSeatsInShow(showId);
      return ResponseEntity.ok(new ApiResponse<>("Available seats retrieved successfully", seats));
    } catch (IllegalArgumentException | DataAccessException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters"));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve available seats"));
    }
  }

  @PostMapping("/lock")
  public ResponseEntity<?> lockSeats(@RequestBody List<Integer> seatIds) {
    try {
      List<Seat> lockedSeats = seatService.getAvailableSeats(seatIds);
      return ResponseEntity.ok(new ApiResponse<>("Seats locked successfully", lockedSeats));
    } catch (IllegalArgumentException | DataAccessException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters"));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to lock seats"));
    }
  }

  @PostMapping("/unlock")
  public ResponseEntity<?> unlockSeats(@RequestBody List<Integer> seatIds) {
    try {
      seatService.unlockSeats(seatIds);
      return ResponseEntity.ok(new ApiResponse<>("Seats unlocked successfully"));
    } catch (IllegalArgumentException | DataAccessException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters"));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to unlock seats"));
    }
  }

  @GetMapping("/status/{showId}")
  public ResponseEntity<?> getSeatStatus(@PathVariable UUID showId) {
    try {
      List<SeatStatusDTO> seatStatus = seatService.getSeatStatusByShowId(showId);
      return ResponseEntity.ok(new ApiResponse<>("Seat status retrieved successfully", seatStatus));
    } catch (IllegalArgumentException | DataAccessException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters"));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve seat status"));
    }
  }

  // GET /seats/{id} – Get a seat by ID
  @GetMapping("/{id}")
  public ResponseEntity<Seat> getSeatById(@PathVariable("id") Integer id) {
    Seat seat = seatService.getSeatById(id); // Ensure this method exists in ISeatService
    return ResponseEntity.ok(seat);
  }

  // PUT /seats/{id} – Update a seat
  @PutMapping("/{id}")
  public ResponseEntity<Seat> updateSeat(@PathVariable("id") Integer id, @RequestBody Seat seat) {
    Seat updatedSeat = seatService.updateSeat(id, seat); // Ensure this method exists in ISeatService
    return ResponseEntity.ok(updatedSeat);
  }

  // DELETE /seats/{id} – Delete a seat
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSeat(@PathVariable("id") Integer id) {
    seatService.deleteSeat(id); // Ensure this method exists in ISeatService
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/screen/{id}")
  public ResponseEntity<?> getSeatsByScreenId(@PathVariable Integer id) {
    List<Seat> Seats = seatRepository.findSeatsByScreenId(id);
    return ResponseEntity.ok(Seats);
  }
}
