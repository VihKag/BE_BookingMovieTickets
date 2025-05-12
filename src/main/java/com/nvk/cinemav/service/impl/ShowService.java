package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.MovieDTO;
import com.nvk.cinemav.dto.ProvinceDTO;
import com.nvk.cinemav.dto.ShowDTO2;
import com.nvk.cinemav.dto.ShowDetailsDTO;
import com.nvk.cinemav.dto.ShowDTO;
import com.nvk.cinemav.dto.TheaterDTO;
import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Province;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.repository.MovieRepository;
import com.nvk.cinemav.repository.ScreenRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.service.IShowService;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShowService implements IShowService {

  private final ShowRepository showRepository;
  private final MovieRepository movieRepository;
  private final ScreenRepository screenRepository;
  public ShowService(ShowRepository showRepository, MovieRepository movieRepository, ScreenRepository screenRepository) {
    this.showRepository = showRepository;
    this.movieRepository = movieRepository;
    this.screenRepository = screenRepository;
  }
  @Override
  public ShowDTO getShowDetails(UUID showId) {
    Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Not found show!"));
    ShowDetailsDTO showDetailsDTO = new ShowDetailsDTO();
    ShowDTO showDTO = new ShowDTO(show);
    return showDTO;
  }

  @Override
  public List<ShowDTO> getListShows(String search) {
    List<Show> shows = showRepository.findUpcomingShows();
    log.info(shows.toString());
    List<ShowDTO> showDTOs = new ArrayList<>();
    for (Show show : shows) {
      ShowDTO showDTO = new ShowDTO(show);
      showDTOs.add(showDTO);
    }
    return showDTOs;
  }

  @Transactional
  @Override
  public String createShow(UUID movie, Integer screen, Date time) {
    Movie mov = movieRepository.findById(movie)
        .orElseThrow(() -> new RuntimeException("Movie not found"));

    Screen scr = screenRepository.findById(screen)
        .orElseThrow(() -> new RuntimeException("Screen not found"));

    Date showTime = (time != null) ? time : Timestamp.from(Instant.now());

    Show newshow = new Show();
    newshow.setMovie(mov);
    newshow.setScreen(scr);
    newshow.setTime(showTime);
    newshow.setAvailableSeats(screenRepository.findById(screen).orElseThrow().getCapacity()); // Số ghế có sẵn bằng sức chứa phòng

    Show savedShow = showRepository.save(newshow);
    return "Created Show: " + savedShow.getId();
  }

  @Override
  public ShowDetailsDTO getShowInforByMovieSlug(String slug) {
    List<Show> shows = showRepository.findAllByMovie_Slug(slug);
    Movie movie = movieRepository.findBySlug(slug);
    MovieDTO movieDTO = new MovieDTO(movie);

    Map<Cinema, List<ShowDTO2>> cinemaShowMap = new HashMap<>();
    Set<Province> provinceSet = new HashSet<>();

    // Gom các ShowDTO theo Cinema
    for (Show show : shows) {
      Screen screen = show.getScreen();
      if (screen == null || screen.getCinema() == null) continue;

      Cinema cinema = screen.getCinema();
      Province province = cinema.getProvince();

      // Chỉ thêm vào nếu province != null
      if (province != null) {
        provinceSet.add(province);
      }

      ShowDTO2 showDTO = new ShowDTO2(show);
      cinemaShowMap.computeIfAbsent(cinema, k -> new ArrayList<>()).add(showDTO);
    }

    // Chuyển từng Cinema + danh sách ShowDTO thành TheaterDTO
    List<TheaterDTO> theaterDTOs = cinemaShowMap.entrySet().stream()
        .map(entry -> new TheaterDTO(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    // Chuyển Province sang DTO
    List<ProvinceDTO> provinceDTOs = provinceSet.stream()
        .map(ProvinceDTO::new)
        .collect(Collectors.toList());

    return new ShowDetailsDTO(movieDTO, provinceDTOs, theaterDTOs);
  }

}
