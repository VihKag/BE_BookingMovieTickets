package com.nvk.cinemav.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nvk.cinemav.dto.BookingDTO;
import com.nvk.cinemav.entity.Seat;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.entity.Ticket;
import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.repository.SeatRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.repository.TicketRepository;
import com.nvk.cinemav.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketConsumer {
  private static final Logger log = LoggerFactory.getLogger(TicketConsumer.class);
  private final ObjectMapper objectMapper;
  private final TicketRepository ticketRepository;
  private final SeatRepository seatRepository;
  private final UserRepository userRepository;
  private final ShowRepository showRepository;

  public TicketConsumer(TicketRepository ticketRepository, SeatRepository seatRepository,
      UserRepository userRepository, ShowRepository showRepository) {
    this.ticketRepository = ticketRepository;
    this.seatRepository = seatRepository;
    this.userRepository = userRepository;
    this.showRepository = showRepository;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule()); // Đăng ký JavaTimeModule
  }

  @KafkaListener(topics = "booking-process", groupId = "booking-group", containerFactory = "kafkaListenerContainerFactory")
  @Transactional
  public void processBooking(String message) {
    try {
      log.info("📥 Received booking request: {}", message);
      BookingDTO bookingDTO = objectMapper.readValue(message, BookingDTO.class);
      log.info("✅ Successfully parsed booking data: {}", bookingDTO);
      BookingDTO bookingInfo = objectMapper.readValue(message, BookingDTO.class);
      User user = userRepository.getReferenceById(bookingInfo.getUserId());
      Show show = showRepository.getReferenceById(bookingInfo.getShowId());

      bookingInfo.getSeatId().forEach(seatId -> {
        Seat seat = seatRepository.getReferenceById(seatId);
        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setPrice(bookingInfo.getPrice());
        ticketRepository.save(ticket);
      });
      log.info("✅ Booking processed successfully for user: {}", user.getId());
    } catch (Exception e) {
      log.error("Error processing booking message: ", e);
      throw new RuntimeException(e);
    }
  }
}
