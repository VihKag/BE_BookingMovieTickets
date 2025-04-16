package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Show;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDTO {
 private Integer id;
 private Integer provinceId;
 private String name;
 private String address;
 private String phone;
 private List<ShowDTO2> showtimes;

 public TheaterDTO(Cinema cinema, List<ShowDTO2> showtimes) {
  this.id = cinema.getId();
  this.provinceId = cinema.getProvince() != null ? cinema.getProvince().getId() : null;
  this.name = cinema.getName();
  this.address = cinema.getAddress();
  this.showtimes = showtimes;
  this.phone = cinema.getPhone();
 }
}
