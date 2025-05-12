package com.nvk.cinemav.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("genres ")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String title;
  private String description;
  private String poster;
  private String video;
  @Column(unique = true)
  private String slug;
  @ManyToMany
  @JoinTable(
      name = "movie_genres",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private Set<Genre> genres = new HashSet<>(); // Evoid NullPointerException
  @OneToMany(mappedBy = "movie")
  private List<Show> shows = new ArrayList<>();
  private Integer duration;
  @Column(name = "`release`")
  private LocalDateTime release;
  @OneToMany(mappedBy = "movie")
  private List<Cast> cast = new ArrayList<>();
  public Movie(UUID movieId, String title, String description, String imageUrl, String videoUrl, String slug, Set<Genre> genres, Integer duration, LocalDateTime release) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.poster = imageUrl;
    this.video = videoUrl;
    this.slug = slug;
    this.genres = genres;
    this.duration = duration;
    this.release = release;
  }
}
