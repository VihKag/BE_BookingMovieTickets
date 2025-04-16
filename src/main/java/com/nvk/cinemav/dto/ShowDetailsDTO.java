package com.nvk.cinemav.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowDetailsDTO {
  private MovieDTO movie;
  private List<ProvinceDTO> provinces;
  private List<TheaterDTO> theaters;
  public ShowDetailsDTO(MovieDTO movie, List<TheaterDTO> theaters) {
    this.movie = movie;

  }
}
