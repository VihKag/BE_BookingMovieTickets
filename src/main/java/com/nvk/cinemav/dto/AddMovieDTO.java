package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Genre;
import com.nvk.cinemav.entity.Show;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddMovieDTO {
  private UUID movieId;
  private String title;
  private String description;
  private MultipartFile poster;
  private MultipartFile video;
  private String slug;
  private Set<Integer> genres;
  private UUID show;
  private Integer duration;
  private LocalDateTime release;
}
