package com.nvk.cinemav.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatStatusDTO {
    private Integer id;
    private String seatNumber;
    private boolean isAvailable;
}
