package com.henry.imdb.backend.domain.dtos;

import com.henry.imdb.backend.domain.models.Cast;
import com.henry.imdb.backend.domain.models.Director;
import com.henry.imdb.backend.domain.models.Genre;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MovieCreateDto {

    private Long id;
    private String title;
    private String synopsis;
    private Integer releaseYear;
    private Integer rating;
    private List<Director> directors;
    private List<Cast> casts;
    private List<Genre> genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieCreateDto that = (MovieCreateDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(synopsis, that.synopsis) && Objects.equals(releaseYear, that.releaseYear) && Objects.equals(rating, that.rating) && Objects.equals(directors, that.directors) && Objects.equals(casts, that.casts) && Objects.equals(genres, that.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, synopsis, releaseYear, rating, directors, casts, genres);
    }
}