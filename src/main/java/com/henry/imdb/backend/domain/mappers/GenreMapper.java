package com.henry.imdb.backend.domain.mappers;

import com.henry.imdb.backend.domain.dtos.GenreDto;
import com.henry.imdb.backend.domain.models.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toGenreDto(Genre genre);
    List<GenreDto>  toGenreDtoList(List<Genre> genreList);
    @Mapping(target = "id", ignore = true)
    Genre toGenre(GenreDto genreDto);
    List<Genre> toGenreList(List<GenreDto> genreDtoList);
}