package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Genre;
import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Show;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private Set<GenreDTO> genres;
  private List<ShowDTO> shows;
  private Integer duration;
  private LocalDateTime release;

  public MovieDTO(Movie movie) {
    this.id = movie.getId();
    this.title = movie.getTitle();
    this.description = movie.getDescription();
    this.poster = movie.getPoster();
    this.video = movie.getVideo();
    this.genres = movie.getGenres().stream().map(GenreDTO::new).collect(Collectors.toSet());
    this.slug = movie.getSlug();
    this.duration = movie.getDuration();
    this.release = movie.getRelease();
  }
}
