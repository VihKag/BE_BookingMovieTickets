package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.AddMovieDTO;
import com.nvk.cinemav.dto.MovieDTO;
import com.nvk.cinemav.repository.MovieRepository;
import com.nvk.cinemav.response.ApiResponse;
import com.nvk.cinemav.service.impl.MovieService;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

  private final MovieRepository movieRepository;
  private final MovieService movieService;

  public MovieController(MovieRepository movieRepository, MovieService movieService) {
    this.movieRepository = movieRepository;
    this.movieService = movieService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getMovieDetails(@PathVariable UUID id) {
    try{
      MovieDTO movieDTO = movieService.getMovieDetails(id);
      return ResponseEntity.ok(new ApiResponse<MovieDTO>("Movie Details!", movieDTO));
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @GetMapping("/list")
  public ResponseEntity<?> getMovieList(){
    try{
      List<MovieDTO> movieDTOList = movieService.getMovieList();
      return ResponseEntity.ok(new ApiResponse<List<MovieDTO>>("Movie list!",movieDTOList));
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> addMovie(@RequestAttribute AddMovieDTO movieDTO) {
    try{
      movieService.createMovie(movieDTO);
      return ResponseEntity.ok(new ApiResponse<String>("Movie Added!"));
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
    try{
      movieService.deleteMovie(id);
      return ResponseEntity.ok(new ApiResponse<String>("Movie Deleted!"));
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch(Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
