package com.henry.imdb.backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MovieDto {

    private String title;
    private Integer releaseYear;
    private Integer rating;
}