package com.nvk.cinemav.entity;

import com.nvk.cinemav.type.TypeSeat;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String seatNumber;
  @Enumerated(EnumType.STRING)
  private TypeSeat type;
  private Boolean status;
  @OneToOne
  private Ticket ticket;
  @ManyToOne
  private Screen screen;
}
