package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.TicketDTO;
import com.nvk.cinemav.entity.Ticket;
import com.nvk.cinemav.kafka.producer.TicketProducer;
import com.nvk.cinemav.repository.TicketRepository;
import com.nvk.cinemav.service.ITicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicketService implements ITicketService {
  private final static Logger log = LoggerFactory.getLogger(TicketService.class);
  private final TicketRepository ticketRepository;
  private final TicketProducer ticketProducer;

  public TicketService(TicketRepository ticketRepository, TicketProducer ticketProducer) {
    this.ticketRepository = ticketRepository;
    this.ticketProducer = ticketProducer;
  }

  @Override
  public void bookTicket(TicketDTO ticket) {
    ticketProducer.sendBookingEvent(ticket);
  }

  @Override
  public void cancelTicket(TicketDTO ticket) {

  }
}
