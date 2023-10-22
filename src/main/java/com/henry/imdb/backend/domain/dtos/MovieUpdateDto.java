package com.henry.imdb.backend.domain.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MovieUpdateDto {
    private String synopsis;
    private Integer rating;
    private List<GenreDto> genres;
}