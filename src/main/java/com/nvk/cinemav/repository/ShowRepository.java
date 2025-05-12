package com.nvk.cinemav.repository;

import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Show;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {
  @Query(value = "SELECT * FROM `show` s WHERE s.movie_id IS NOT NULL " +
      "AND DATE_ADD(s.time, INTERVAL s.movie.duration MINUTE) < :now",
      nativeQuery = true)
  List<Show> findFinishedShows(@Param("now") LocalDateTime now);

  @Query(value = "SELECT * FROM `show` s WHERE s.time > NOW()", nativeQuery = true)
  List<Show> findUpcomingShows();

  List<Show> findAllByMovie_Slug(String slug);

  @Query(value = "SELECT * FROM `show` s WHERE s.time > NOW() AND s.movie_id = :movieId", nativeQuery = true)
  List<Show> findUpcomingShowsByMovieId(@Param("movieId") UUID movieId);
}
