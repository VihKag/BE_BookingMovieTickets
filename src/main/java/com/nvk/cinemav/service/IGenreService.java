package com.nvk.cinemav.service;

import com.nvk.cinemav.dto.GenreDTO;
import com.nvk.cinemav.entity.Genre;
import java.util.List;

public interface IGenreService {
  void createGenre(GenreDTO genre);
  List<Genre> getGenreList();
  Genre getGenreById(int id);
}
