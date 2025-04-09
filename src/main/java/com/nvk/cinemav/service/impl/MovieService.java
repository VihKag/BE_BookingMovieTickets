package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.cloudinary.CloudinaryService;
import com.nvk.cinemav.dto.AddMovieDTO;
import com.nvk.cinemav.dto.GenreDTO;
import com.nvk.cinemav.dto.MovieDTO;
import com.nvk.cinemav.dto.ShowDTO;
import com.nvk.cinemav.entity.Genre;
import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.repository.GenreRepository;
import com.nvk.cinemav.repository.MovieRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.service.IMovieService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MovieService implements IMovieService {

  private final MovieRepository movieRepository;
  private final GenreRepository genreRepository;
  private final ShowRepository showRepository;
  private final CloudinaryService cloudinaryService;

  public MovieService(MovieRepository movieRepository, GenreRepository genreRepository,
      ShowRepository showRepository,
      CloudinaryService cloudinaryService) {
    this.movieRepository = movieRepository;
    this.genreRepository = genreRepository;
    this.showRepository = showRepository;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  public MovieDTO getMovieDetails(UUID movieId) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new RuntimeException("Movie not found"));
    return new MovieDTO(movie);
  }

  @Override
  public MovieDTO getMovieBySlug(String slug) {
    Movie movie = movieRepository.findBySlug(slug);
    MovieDTO movieDTO = new MovieDTO(movie);
    return movieDTO;
  }

  @Override
  public List<MovieDTO> getMovieList() {
    List<Movie> movies = movieRepository.findAll();
    return movies.stream().map(
        movie ->
            new MovieDTO(movie)
    ).collect(Collectors.toList());
  }

  @Transactional
  @Override
  public void createMovie(AddMovieDTO movieDTO) {
    Set<Genre> genres = new HashSet<>(genreRepository.findAllById(movieDTO.getGenres()));
    String imageUrl = cloudinaryService.uploadImage(movieDTO.getPoster());
    String videoUrl = cloudinaryService.uploadVideo(movieDTO.getVideo());
    Movie movie = new Movie(
        movieDTO.getMovieId(),
        movieDTO.getTitle(),
        movieDTO.getDescription(),
        imageUrl,
        videoUrl,
        movieDTO.getSlug(),
        genres,
        movieDTO.getDuration(),
        movieDTO.getRelease()
    );
    movieRepository.save(movie);
  }

  @Transactional
  @Override
  public void deleteMovie(UUID movieId) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new RuntimeException("Movie not found"));
    cloudinaryService.deleteFileByUrl(movie.getPoster(), "image");
    cloudinaryService.deleteFileByUrl(movie.getVideo(), "video");
    movieRepository.delete(movie);
  }
}
