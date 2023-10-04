package com.henry.imdb.backend.controllers;

import com.henry.imdb.backend.domain.dtos.MovieDetailDto;
import com.henry.imdb.backend.domain.dtos.MovieDto;
import com.henry.imdb.backend.services.MoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MoviesController {

    private final MoviesService moviesService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> allMovies() {
        return ResponseEntity.ok(moviesService.allMovies());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDetailDto> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(moviesService.getMovie(id));
    }
}