package com.nvk.cinemav.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String title;
  private String description;
  private String poster;
  @Column(unique = true)
  private String slug;
  @OneToMany(mappedBy = "movie")
  private Set<Genre> genres;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "show_id", referencedColumnName = "id")
  private Show show;
  private Integer duration;
  @Column(name = "`release`")
  private LocalDateTime release;
  
}
