package com.henry.imdb.backend.domain.mappers;

import com.henry.imdb.backend.domain.dtos.MovieDetailDto;
import com.henry.imdb.backend.domain.dtos.MovieDto;
import com.henry.imdb.backend.domain.models.Movie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto toMovieDto(Movie movie);

    MovieDetailDto toMovieDetailDto(Movie movie);
}