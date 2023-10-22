package com.henry.imdb.backend.controllers;

import com.henry.imdb.backend.domain.dtos.MovieCreateDto;
import com.henry.imdb.backend.domain.dtos.MovieDetailDto;
import com.henry.imdb.backend.domain.dtos.MovieDto;
import com.henry.imdb.backend.domain.dtos.MovieUpdateDto;
import com.henry.imdb.backend.services.MoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    @PatchMapping("/movies/{id}")
    public ResponseEntity<MovieUpdateDto> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieUpdateDto movieUpdateDto) {
        MovieUpdateDto updatedMovie = moviesService.updateMovie(id, movieUpdateDto);
        return ResponseEntity.ok(updatedMovie);
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieCreateDto> createMovie(
            @RequestBody
            MovieCreateDto movieCreateDto){
            MovieCreateDto createdMovie = moviesService.createMovie(movieCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMovie.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdMovie);
    }
}