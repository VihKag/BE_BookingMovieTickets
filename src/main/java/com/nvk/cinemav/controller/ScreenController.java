package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.ScreenDTO;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.service.impl.ScreenService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/screens")
public class ScreenController {

  private final ScreenService screenService;

  public ScreenController(ScreenService screenService) {
    this.screenService = screenService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getScreen(@PathVariable int id) {
    try{
      ScreenDTO screen = screenService.getScreen(id);
      return ResponseEntity.ok(screen);
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @GetMapping("/list")
  public ResponseEntity<?> getScreens() {
    try{
      List<ScreenDTO> sceerns = screenService.getScreenList();
      return ResponseEntity.ok(sceerns);
    }
    catch(IllegalArgumentException | DataAccessException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    catch (Exception e){
      e.printStackTrace();
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
