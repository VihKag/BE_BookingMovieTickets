package com.nvk.cinemav.repository;

import com.nvk.cinemav.entity.Seat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
  @Modifying
  @Query("UPDATE Seat s SET s.status = :status WHERE s.screen.id IN " +
      "(SELECT sh.screen.id FROM Show sh WHERE sh.id = :showId)")
  void updateSeatStatusByShow(UUID showId, Boolean status);
}
