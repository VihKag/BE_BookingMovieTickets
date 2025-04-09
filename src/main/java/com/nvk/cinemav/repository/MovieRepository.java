package com.nvk.cinemav.repository;

import com.nvk.cinemav.dto.MovieDTO;
import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Show;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
  Movie findBySlug(String slug);
}
