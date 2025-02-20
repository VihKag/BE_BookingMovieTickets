package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.ShowDTO;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IShowService {
  ShowDTO getShowDetails(UUID showId);
  List<ShowDTO> getListShows(String search);
  String createShow(UUID showId, Integer screen, Date time);

}
