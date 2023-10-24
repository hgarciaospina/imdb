package com.henry.imdb.backend.domain.dtos;

import com.henry.imdb.backend.domain.models.Cast;
import com.henry.imdb.backend.domain.models.Director;
import com.henry.imdb.backend.domain.models.Genre;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder

public class MovieCreateDto {
    @NotNull(message="Must have an id")
    private Long id;
    @NotEmpty(message="Must have a title")
    @NotNull(message="Must have a title")
    @NotBlank(message="Must have a title")
    private String title;
    @NotEmpty(message="Must have a synopsis")
    @NotNull(message="Must have a synopsis")
    @NotBlank(message="Must have a synopsis")
    private String synopsis;
    @NotNull(message="Must have a release year")
    private Integer releaseYear;
    @NotNull(message="Must have a rating")
    @Min(value = 1,message="The score must be between 1 and 5.")
    @Max(value = 5,message="The score must be between 1 and 5.")
    private Integer rating;
    private List<Director> directors;
    private List<Cast> casts;
    private List<Genre> genres;
}
