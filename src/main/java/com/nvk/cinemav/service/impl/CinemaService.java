package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.CinemaDTO;
import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.repository.CinemaRepository;
import com.nvk.cinemav.service.ICinemaService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CinemaService implements ICinemaService {
  private final CinemaRepository cinemaRepository;
  public CinemaService(CinemaRepository cinemaRepository) {
    this.cinemaRepository = cinemaRepository;
  }
  @Override
  public Cinema getCinemaDetails(int id) {
    return cinemaRepository.findById(id).orElseThrow(()-> new RuntimeException("Cinema not found"));
  }
  @Transactional
  @Override
  public void updateCinemaInfor(CinemaDTO cinemaDTO) {
    Cinema cinema = cinemaRepository.findById(cinemaDTO.getId()).orElseThrow(()-> new RuntimeException("Cinema not found"));
    cinema.setName(cinemaDTO.getName());
    cinema.setAddress(cinemaDTO.getAddress());
    cinema.setCountry(cinemaDTO.getCountry());
    cinemaRepository.save(cinema);
  }

  @Override
  public List<Cinema> getCinemas() {
    return cinemaRepository.findAll();
  }
}