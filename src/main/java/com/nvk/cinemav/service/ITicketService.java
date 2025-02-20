package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.TicketDTO;
import com.nvk.cinemav.entity.Ticket;

public interface ITicketService {
  void bookTicket(TicketDTO ticket);
  void cancelTicket(TicketDTO ticket);
}
