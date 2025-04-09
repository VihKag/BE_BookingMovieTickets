package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.CinemaDTO;
import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.mapper.CinemaMapper;
import com.nvk.cinemav.repository.CinemaRepository;
import com.nvk.cinemav.service.ICinemaService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    cinemaRepository.save(cinema);
  }

  @Override
  public List<CinemaDTO> getCinemas() {
    List<Cinema> cinemas = cinemaRepository.findCinemasWithShows();
    List<CinemaDTO> cinemasDTO = cinemas.stream().map(
        CinemaMapper::toCinemaDTO).collect(Collectors.toList());
    return cinemasDTO;
  }
}