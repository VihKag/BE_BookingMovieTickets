package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Genre;
import com.nvk.cinemav.entity.Show;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
  private UUID id;
  private String title;
  private String description;
  private String poster;
  private String video;
  private String slug;
  private Set<Genre> genres;
  private Show show;
  private Integer duration;
  private LocalDateTime release;
}
