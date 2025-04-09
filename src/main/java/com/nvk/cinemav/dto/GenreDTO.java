package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private Integer id;
    private String name;
    private String slug;
    public GenreDTO(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
        this.slug = genre.getSlug();
    }
}
