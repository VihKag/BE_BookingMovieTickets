package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.entity.Seat;
import com.nvk.cinemav.entity.Show;
import com.nvk.cinemav.repository.SeatRepository;
import com.nvk.cinemav.repository.ShowRepository;
import com.nvk.cinemav.service.ISeatService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService implements ISeatService {
  private final SeatRepository seatRepository;
  private final ShowRepository showRepository;
  @Scheduled(fixedRate = 60000) // Chạy mỗi phút
  @Transactional
  @Override
  public void updateSeatStatusAfterShow() {
    LocalDateTime now = LocalDateTime.now();

    // Lấy danh sách suất chiếu đã kết thúc
    List<Show> finishedShows = showRepository.findFinishedShows(now);

    for (Show show : finishedShows) {
      log.info("🔄 Cập nhật trạng thái ghế cho suất chiếu: {}", show.getId());

      // Cập nhật tất cả ghế của suất chiếu đó thành available (true)
      seatRepository.updateSeatStatusByShow(show.getId(), true);
    }
  }

  @Override
  public List<Seat> getAvailableSeats(List<Integer> seatIds) {
    return List.of();
  }

}
