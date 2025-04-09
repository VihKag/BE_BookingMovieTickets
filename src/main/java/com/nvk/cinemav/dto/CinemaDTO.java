package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Cinema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDTO {
  private Integer id;
  private String name;
  private String address;
  private List<ScreenDTO> screenDTO;

  public CinemaDTO(Cinema cinema) {
    id = cinema.getId();
    name = cinema.getName();
    address = cinema.getAddress();
  }
}
