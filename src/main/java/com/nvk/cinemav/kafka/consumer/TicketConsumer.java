package com.nvk.cinemav.kafka.consumer;

import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.entity.Seat;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.entity.Ticket;
import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.kafka.producer.PaymentProducer;
import com.nvk.cinemav.repository.SeatRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.repository.TicketRepository;
import com.nvk.cinemav.repository.UserRepository;
import com.nvk.cinemav.service.impl.TicketService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketConsumer {
  private static  final Logger log = LoggerFactory.getLogger(TicketConsumer.class);
  private final KafkaTemplate<String, BookingDTO> kafkaTemplate;
  private final KafkaTemplate<String, String> stringKafkaTemplate;
  private final TicketRepository ticketRepository;
  private final TicketService ticketService;
  private final SeatRepository seatRepository;
  private final UserRepository userRepository;
  private final ShowRepository showRepository;
  private final PaymentProducer paymentProducer;

  public TicketConsumer(KafkaTemplate<String, BookingDTO> kafkaTemplate , KafkaTemplate<String, String> stringKafkaTemplate,
      TicketRepository ticketRepository, TicketService ticketService, SeatRepository seatRepository,
      UserRepository userRepository, ShowRepository showRepository, PaymentProducer paymentProducer) {
    this.kafkaTemplate = kafkaTemplate;
    this.stringKafkaTemplate = stringKafkaTemplate;
    this.ticketRepository = ticketRepository;
    this.ticketService = ticketService;
    this.seatRepository = seatRepository;
    this.userRepository = userRepository;
    this.showRepository = showRepository;
    this.paymentProducer = paymentProducer;
  }

  @KafkaListener(topics = "booking-process", groupId = "booking-group")
  @Transactional
  public void processBooking(BookingDTO bookingInfo) {
    try{
      User user = userRepository.getReferenceById(bookingInfo.getUserId());
      Show show = showRepository.getReferenceById(bookingInfo.getShowId());
      bookingInfo.getSeatId().stream().forEach(seatId -> {
        Ticket ticket = new Ticket();
        Seat seat = seatRepository.getReferenceById(seatId);
        ticket.setPrice(bookingInfo.getPrice());
        ticket.setSeat(seat);
        ticket.setShow(show);
        ticket.setUser(user);
        ticketRepository.save(ticket);
      });
    }
    catch(Exception e){
      log.error(e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }
}
