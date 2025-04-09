package com.nvk.cinemav.mapper;

import com.nvk.cinemav.dto.CinemaDTO;
import com.nvk.cinemav.dto.ScreenDTO;
import com.nvk.cinemav.dto.ShowDTO;
import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Show;
import java.util.List;
import java.util.stream.Collectors;

public class CinemaMapper {

  public static ShowDTO toShowDTO(Show show) {
    return new ShowDTO(
        show
    );
  }

  public static ScreenDTO toScreenDTO(Screen screen) {
    List<ShowDTO> showDTOs = screen.getShows()
        .stream()
        .map(CinemaMapper::toShowDTO)
        .collect(Collectors.toList());

    return new ScreenDTO(
        screen.getId(),
        screen.getName(),
        screen.getCapacity(),
        showDTOs
    );
  }

  public static CinemaDTO toCinemaDTO(Cinema cinema) {
    List<ScreenDTO> screenDTOs = cinema.getScreens()
        .stream()
        .map(CinemaMapper::toScreenDTO)
        .collect(Collectors.toList());

    return new CinemaDTO(
        cinema.getId(),
        cinema.getName(),
        cinema.getAddress(),
        screenDTOs
    );
  }
}
