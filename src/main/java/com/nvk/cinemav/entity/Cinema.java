package com.nvk.cinemav.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Cinema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String address;
  @OneToMany(mappedBy = "cinema")
  private List<Screen> screens;
  private String country;
  public Cinema(Cinema cinema) {
    this.id = cinema.getId();
    this.name = cinema.getName();
    this.address = cinema.getAddress();
    this.country = cinema.getCountry();
  }
}
