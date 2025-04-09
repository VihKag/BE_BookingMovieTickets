package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.CinemaDTO;
import com.nvk.cinemav.entity.Cinema;
import java.util.List;

public interface ICinemaService {
  Cinema getCinemaDetails(int id);
  void updateCinemaInfor(CinemaDTO cinemaDTO);
  List<CinemaDTO> getCinemas();
}
