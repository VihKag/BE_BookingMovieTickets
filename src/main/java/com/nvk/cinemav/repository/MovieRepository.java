package com.nvk.cinemav.repository;

import com.nvk.cinemav.entity.Movie;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

}
