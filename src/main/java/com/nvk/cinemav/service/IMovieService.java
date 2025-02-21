package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.AddMovieDTO;
import com.nvk.cinemav.dto.MovieDTO;
import java.util.List;
import java.util.UUID;

public interface IMovieService {
  MovieDTO getMovieDetails(UUID movieId);
  List<MovieDTO> getMovieList();
  void createMovie(AddMovieDTO movieDTO);
  void deleteMovie(UUID movieId);
}
