package com.nvk.cinemav.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Screen {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  @OneToMany(mappedBy = "screen")
  private List<Seat> seats = new ArrayList<>();
  private Integer capacity;
  @OneToMany(mappedBy = "screen")
  @BatchSize(size = 10)
  private List<Show> shows = new ArrayList<>();
  @ManyToOne(cascade = CascadeType.ALL)
  private Cinema cinema;
}
