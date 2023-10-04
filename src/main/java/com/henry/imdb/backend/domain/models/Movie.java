package com.henry.imdb.backend.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Movie {

    private Long id;
    private String title;
    private String synopsis;
    private Integer releaseYear;
    private Integer rating;
    private List<Director> directors;
    private List<Cast> casts;
    private List<Genre> genres;

}
