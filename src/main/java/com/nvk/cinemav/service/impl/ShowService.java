package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.ShowDTO;
import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.repository.MovieRepository;
import com.nvk.cinemav.repository.ScreenRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.service.IShowService;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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
    ShowDTO showDTO = new ShowDTO();
    showDTO.setId(show.getId());
    showDTO.setAvailableSeats(show.getAvailableSeats());
    showDTO.setMovie(show.getMovie());
    showDTO.setScreen(show.getScreen());
    showDTO.setTime(show.getTime());
    return showDTO;
  }

  @Override
  public List<ShowDTO> getListShows(String search) {
    List<Show> shows = showRepository.findUpcomingShows();
    return shows.stream().map(
        show -> new ShowDTO(
            show.getId(),
            show.getMovie(),
            show.getScreen(),
            show.getTime(),
            show.getAvailableSeats()
        )
    ).collect(Collectors.toList());
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

}
