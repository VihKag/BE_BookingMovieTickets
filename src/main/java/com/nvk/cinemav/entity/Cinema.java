package com.nvk.cinemav.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Cinema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String address;
  private String phone;
  @OneToMany(mappedBy = "cinema")
  @BatchSize(size = 10)
  private List<Screen> screens;
  @ManyToOne(cascade = CascadeType.ALL)
  private Province province;
  public Cinema(Cinema cinema) {
    this.id = cinema.getId();
    this.name = cinema.getName();
    this.address = cinema.getAddress();
    this.province = cinema.getProvince();
  }
}
