package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.AddShowDTO;
import com.nvk.cinemav.dto.ShowDTO;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.service.IShowService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shows")
public class ShowController {
  private final IShowService showService;
  public ShowController(IShowService showService) {
    this.showService = showService;
  }

  @GetMapping("/up_comming")
  public ResponseEntity<?> getShowsUpComing(@RequestParam(required = false, defaultValue = "") String search) {
    try{
      List<ShowDTO> shows = showService.getListShows(search);
      return ResponseEntity.ok(shows);
    }
    catch(IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch(DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getShowDetails(@PathVariable UUID id) {
    try{
      ShowDTO show = showService.getShowDetails(id);
      return ResponseEntity.ok(show);
    }
    catch(IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch(DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createShow(@RequestBody AddShowDTO show) {
    try{
      showService.createShow(show.getMovieId(), show.getScreenId(), show.getTime());
      return ResponseEntity.ok("Success!");
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
