package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.repository.ScreenRepository;
import com.nvk.cinemav.service.IScreenService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScreenService implements IScreenService {
  private final ScreenRepository screenRepository;
  public ScreenService(ScreenRepository screenRepository) {
    this.screenRepository = screenRepository;
  }

  @Override
  public Screen getScreen(int id) {
    return screenRepository.findById(id).orElseThrow(()-> new RuntimeException("Screen not found"));
  }

  @Override
  public List<Screen> getScreenList() {
    return screenRepository.findAll();
  }
}
