package com.henry.imdb.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.henry.imdb.backend.domain.dtos.GenreDto;
import com.henry.imdb.backend.domain.dtos.MovieDetailDto;
import com.henry.imdb.backend.domain.dtos.MovieDto;
import com.henry.imdb.backend.domain.dtos.MovieUpdateDto;
import com.henry.imdb.backend.domain.exceptions.AppException;
import com.henry.imdb.backend.domain.mappers.GenreMapper;
import com.henry.imdb.backend.domain.mappers.MovieMapper;
import com.henry.imdb.backend.domain.models.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class MoviesServiceTest {
    @InjectMocks
    private MoviesService moviesService;
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private GenreMapper genreMapper;
    @Test
    void allMovies() {
        // Configure the behavior of the MovieMapper mock
        when(movieMapper.toMovieDto(any())).thenReturn(new MovieDto());

        List<MovieDto> result = moviesService.allMovies();

        // Verify that the resulting list is not null and has the same number of elements as the example movie list
        assertNotNull(result);
        assertEquals(4, result.size());

        // Verify that movieMapper.toMovieDto() has been called 4 times (once for each movie)
        verify(movieMapper, times(4)).toMovieDto(any());
    }

    @Test
    void getMovie() {
        Long movieId = 1L;

        // Configure MovieMapper mock behavior
        when(movieMapper.toMovieDetailDto(any(Movie.class))).thenReturn(new MovieDetailDto());

        // Call the method to be tested
        MovieDetailDto movieDetailDto = moviesService.getMovie(movieId);

        // Check that the result is not null
        assertNotNull(movieDetailDto);
    }

    @Test
    void getMovieNotFound() {
        // ID that does not exist in the fictional movie list
        Long nonExistentMovieId = 99L;

        // Configure MovieMapper mock behavior
        when(movieMapper.toMovieDetailDto(any(Movie.class))).thenReturn(new MovieDetailDto());

        // Wait for AppException to be thrown with HttpStatus.NOT_FOUND
        AppException exception = assertThrows(AppException.class, () ->
                moviesService.getMovie(nonExistentMovieId));

        // Check that the result is null (movie not found)
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        // You can also verify that movieMapper.toMovieDetailDto() was not called in this case
        verify(movieMapper, never()).toMovieDetailDto(any(Movie.class));
    }

    @Test
    void updateMovie() {
        Long movieId = 1L;
        MovieUpdateDto movieUpdateDto = MovieUpdateDto.builder()
                .synopsis("Update Synopsys")
                .rating(5)
                .genres(List.of(new GenreDto("Genre-Update-1"),
                        new GenreDto("Genre-Update-2")))
                .build();
        when(movieMapper.toMovieUpdateDto(any(Movie.class))).thenReturn(movieUpdateDto);

        MovieUpdateDto result = moviesService.updateMovie(movieId, movieUpdateDto);

        assertEquals(movieUpdateDto, result);

        verify(movieMapper,times(1)).toMovieUpdateDto(any(Movie.class));
    }

    @Test
    void updateMovieNotFound() {
        Long movieId = 99L;
        MovieUpdateDto movieUpdateDto = MovieUpdateDto.builder()
                .build();

        // Wait for AppException to be thrown with HttpStatus.NOT_FOUND
        AppException exception = assertThrows(AppException.class, () ->
                moviesService.updateMovie(movieId, movieUpdateDto));

        // Check that the result is null (movie not found)
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        // You can also verify that movieMapper.toMovieDetailDto() was not called in this case
        verify(movieMapper, never()).toMovieUpdateDto(any(Movie.class));
    }
}