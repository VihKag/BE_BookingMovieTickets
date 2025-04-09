package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.CinemaDTO;
import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.service.ICinemaService;
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
@RequestMapping("/cinemas")
public class CinemaController {
  private ICinemaService cinemaService;
  public CinemaController(ICinemaService cinemaService) {
    this.cinemaService = cinemaService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getCinema(@PathVariable int id) {
    try{
      Cinema cinema = cinemaService.getCinemaDetails(id);
      return ResponseEntity.ok(cinema);
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @GetMapping("/list")
  public ResponseEntity<?> getCinemaList() {
    try{
      List<CinemaDTO> cinemas = cinemaService.getCinemas();
      return ResponseEntity.ok(cinemas);
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @PostMapping("/{id}")
  public ResponseEntity<?> updateCinema(@RequestBody CinemaDTO cinemaDTO) {
    try{
      cinemaService.updateCinemaInfor(cinemaDTO);
      return ResponseEntity.ok("Updated Successfully");
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e){
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

}
