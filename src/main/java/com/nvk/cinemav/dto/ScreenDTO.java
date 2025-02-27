package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Cinema;
import com.nvk.cinemav.entity.Screen;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDTO {
    private Integer id;
    private String name;
    private Integer capacity;
    private Cinema cinema;
    private List<ShowDTO> shows;

    public ScreenDTO(Screen screen) {
        this.id = screen.getId();
        this.name = screen.getName();
        this.capacity = screen.getCapacity();

    }
}
