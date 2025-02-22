package com.nvk.cinemav.service;

import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Seat;
import java.util.List;

public interface IScreenService {
  Screen getScreen(int id);
  List<Screen> getScreenList();
}
