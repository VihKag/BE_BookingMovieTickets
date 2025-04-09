package com.nvk.cinemav.repository;

import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.entity.Movie;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
  // Method to find cinemas with screens that have shows
  @Query("SELECT DISTINCT c FROM Cinema c " +
      "JOIN c.screens s " +
      "JOIN s.shows sh " +
      "WHERE sh IS NOT NULL")
  List<Cinema> findCinemasWithShows();

  // Method to find cinemas with shows for a specific movie
  @Query("SELECT DISTINCT c FROM Cinema c " +
      "JOIN c.screens s " +
      "JOIN s.shows sh " +
      "WHERE sh.movie.id = :movieId")
  List<Cinema> findCinemasWithShowsByMovie(@Param("movieId") UUID movieId);

  @Query("SELECT c FROM Cinema c WHERE c.address = :address")
  List<Cinema> findByProvinceId(String address);

}
