package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.response.ApiResponse;
import com.nvk.cinemav.service.ITicketService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {
  private final ITicketService ticketService;

  public TicketController(ITicketService ticketService) {
    this.ticketService = ticketService;
  }

  @PostMapping("booking")
  public ResponseEntity<?> bookingTicket(@RequestBody BookingDTO ticket){
    try{
      ticketService.bookTicket(ticket);
      return ResponseEntity.ok("Booking request send!");
    }
    catch (IllegalArgumentException | DataAccessException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters!"));
    }
    catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Booking request failed!");
    }
  }
}
