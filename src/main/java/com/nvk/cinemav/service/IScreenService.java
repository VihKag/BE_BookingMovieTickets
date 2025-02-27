package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.ScreenDTO;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Seat;
import java.util.List;

public interface IScreenService {
  ScreenDTO getScreen(int id);
  List<ScreenDTO> getScreenList();
}
