package com.nvk.cinemav.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`show`")
public class Show {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @OneToMany(mappedBy = "show")
  private Set<Ticket> tickets;
  @OneToOne(mappedBy = "show")
  private Movie movie;
  @OneToOne(mappedBy = "show")
  private Screen screen;
  private Date time;
  private Integer availableSeats;
}
