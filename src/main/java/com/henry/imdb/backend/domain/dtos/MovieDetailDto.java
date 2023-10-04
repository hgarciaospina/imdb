package com.henry.imdb.backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MovieDetailDto {

    private String title;
    private Integer releaseYear;
    private Integer rating;
    List<DirectorDto> directors;
    List<CastDto> casts;
    List<GenreDto> genres;
}
