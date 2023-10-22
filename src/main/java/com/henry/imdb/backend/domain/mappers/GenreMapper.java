package com.henry.imdb.backend.domain.mappers;

import com.henry.imdb.backend.domain.dtos.GenreDto;
import com.henry.imdb.backend.domain.models.Genre;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toGenreDto(Genre genre);
    List<GenreDto>  toGenreDtoList(List<Genre> genreList);
    Genre toGenre(GenreDto genreDto);
    List<Genre> toGenreList(List<GenreDto> genreDtoList);
}