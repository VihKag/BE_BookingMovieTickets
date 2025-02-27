package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Screen;
import com.nvk.cinemav.entity.Show;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowDTO {
    private UUID id;
    private MovieDTO movie;
    private ScreenDTO screen;
    private Date time;
    private Integer availableSeats;

    public ShowDTO(Show show) {
        this.id = show.getId();
        this.movie = new MovieDTO(show.getMovie());
        this.screen = new ScreenDTO(show.getScreen());
        this.time = show.getTime();
        this.availableSeats = show.getAvailableSeats();
    }
}
