package com.nvk.cinemav.repository;

import com.nvk.cinemav.entity.Seat;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
  @Modifying
  @Query(value="UPDATE Seat s SET s.status = :status WHERE s.screen.id IN " +
      "(SELECT sh.screen.id FROM Show sh WHERE sh.id = :showId)", nativeQuery = true)
  void updateSeatStatusByShow(UUID showId, Boolean status);
  //get seats available in a show
  @Query("SELECT s FROM Seat s WHERE s.screen.id IN " +
      "(SELECT sc.id FROM Screen sc WHERE sc.shows.id = :showId) " +
      "AND s.bookedSeats IS EMPTY")
  List<Seat> findAvailableSeatsByShowId(@Param("showId") UUID showId);
  @Query("SELECT s FROM Seat s WHERE s.screen.id = :screanId")
  List<Seat> findSeatsByScreenId(Integer screenId);
  @Query("SELECT s FROM Seat s WHERE s.screen.id IN " +
         "(SELECT sc.id FROM Screen sc WHERE sc.shows.id = :showId)")
  List<Seat> findSeatsByShowId(@Param("showId") UUID showId);
  @Modifying
  @Query("UPDATE Seat s SET s.bookedSeats = NULL WHERE s.id IN :seatIds")
  void unlockSeats(@Param("seatIds") List<Integer> seatIds);
  @Modifying
  @Query("UPDATE Seat s SET s.bookedSeats = NULL WHERE s.screen.id IN " +
         "(SELECT sc.id FROM Screen sc WHERE sc.shows.id = :showId)")
  void updateSeatStatusByShow(@Param("showId") UUID showId, @Param("available") boolean available);
}
