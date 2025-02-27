package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.GenreDTO;
import com.nvk.cinemav.entity.Genre;
import com.nvk.cinemav.service.impl.GenreService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genres")
public class GenreController {

  private final GenreService genreService;

  public GenreController(GenreService genreService) {
    this.genreService = genreService;
  }

  @GetMapping("/list")
  public ResponseEntity<?> getGenres() {
    try{
      List<Genre> genres = genreService.getGenreList();
      return ResponseEntity.ok(genres);
    }
    catch (IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getGenreById(@PathVariable int id) {
    try{
      Genre genre = genreService.getGenreById(id);
      return ResponseEntity.ok(genre);
    }
    catch (IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e){
    return ResponseEntity.internalServerError().body(e.getMessage());}
  }

  @PostMapping("")
  public ResponseEntity<?> createGenre(@RequestBody GenreDTO genreDTO) {
    try{
      genreService.createGenre(genreDTO);
      return ResponseEntity.ok().body("Genre created");
    }
    catch (IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
