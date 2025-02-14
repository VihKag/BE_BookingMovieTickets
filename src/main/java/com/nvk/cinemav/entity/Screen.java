package com.nvk.cinemav.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private List<Seat> seats;
  private Integer capacity;
  // Reference to the Cinema entity
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "show_id", referencedColumnName = "id")
  private Show show;
  @ManyToOne
  private Cinema cinema;
}
