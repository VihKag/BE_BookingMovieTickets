package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.dto.GenreDTO;
import com.nvk.cinemav.entity.Genre;
import com.nvk.cinemav.repository.GenreRepository;
import com.nvk.cinemav.service.IGenreService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenreService implements IGenreService {
  private final GenreRepository genreRepository;
  public GenreService(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  @Override
  public void createGenre(GenreDTO genreDto) {
    Genre genre = new Genre();
    genre.setId(genreDto.getId());
    genre.setName(genreDto.getName());
    genre.setSlug(genreDto.getSlug());
    genreRepository.save(genre);
  }


  @Override
  public List<Genre> getGenreList() {
    List<Genre> genreList = genreRepository.findAll();

    return genreList;
  }

  @Override
  public Genre getGenreById(int id) {
    Genre genre = genreRepository.findById(id).orElseThrow(()-> new RuntimeException("Genre not found"));
    return genre;
  }
}
