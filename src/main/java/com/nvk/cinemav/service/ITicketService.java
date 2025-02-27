package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.BookingDTO;

public interface ITicketService {
  void bookTicket(BookingDTO ticket);
  void cancelTicket(BookingDTO ticket);
}
