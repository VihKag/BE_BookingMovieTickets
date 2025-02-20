package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Movie;
import com.nvk.cinemav.entity.Screen;
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
    private Movie movie;
    private Screen screen;
    private Date time;
    private Integer availableSeats;
}
