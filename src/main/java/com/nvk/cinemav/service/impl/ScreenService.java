package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.ScreenDTO;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.repository.ScreenRepository;
import com.nvk.cinemav.service.IScreenService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScreenService implements IScreenService {
  private final ScreenRepository screenRepository;
  public ScreenService(ScreenRepository screenRepository) {
    this.screenRepository = screenRepository;
  }

  @Override
  public ScreenDTO getScreen(int id) {
    Screen screen = screenRepository.findById(id).orElseThrow(()-> new RuntimeException("Screen not found"));
    ScreenDTO screenDTO = new ScreenDTO();
    screenDTO.setId(screen.getId());
    screenDTO.setName(screen.getName());
    screenDTO.setCapacity(screen.getCapacity());
    screenDTO.setCinema(null);
    screenDTO.setShows(null);
    return screenDTO;
  }

  @Override
  public List<ScreenDTO> getScreenList() {
    List<Screen> screens = screenRepository.findAll();
    List<ScreenDTO> screenDTOList = new ArrayList<>();
    for (Screen screen : screens) {
      ScreenDTO screenDTO = new ScreenDTO();
      screenDTO.setId(screen.getId());
      screenDTO.setName(screen.getName());
      screenDTO.setCapacity(screen.getCapacity());
      screenDTO.setCinema(null);
      screenDTO.setShows(null);
      screenDTOList.add(screenDTO);
    }
    return screenDTOList;
  }
}
