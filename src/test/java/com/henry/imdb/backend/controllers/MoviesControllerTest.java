package com.henry.imdb.backend.controllers;

import com.henry.imdb.backend.ImdbApplication;
import com.henry.imdb.backend.config.RestExceptionHandler;
import com.henry.imdb.backend.domain.dtos.*;
import com.henry.imdb.backend.domain.exceptions.AppException;
import com.henry.imdb.backend.services.MoviesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = ImdbApplication.class)
class MoviesControllerTest {
    @MockBean
    private MoviesService moviesService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Assign a MovieController instance
        MoviesController moviesController = new MoviesController(moviesService);

        // Configure MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(moviesController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void allMovies() throws Exception {
        when(moviesService.allMovies()).thenReturn(Arrays.asList(
                new MovieDto("The Shawshank Redemption",1984,5),
                new MovieDto("The Godfather",1975,5)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("The Shawshank Redemption"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1984))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("The Godfather"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").value(1975))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rating").value(5));
    }

    @Test
    void getMovie()  throws Exception{
        // Fictitious movie ID
        Long movieId = 1L;

        // Movie director
        DirectorDto directorDto = new DirectorDto("Frank Darabont");
        var listDirector = List.of(directorDto);

        // Casts
        CastDto castDto1 = new CastDto("Tim Robbins");
        CastDto castDto2 = new CastDto("Morgan Freeman");
        var listCast = List.of(castDto1, castDto2);

        // Genres
        GenreDto genreDto1 = new GenreDto("Drama");
        GenreDto genreDto2 = new GenreDto("Crime");
        var listGenre = List.of(genreDto1, genreDto2);

        // Configuring the mock service to return a mock movie
        when(moviesService.getMovie(movieId)).thenReturn
                (new MovieDetailDto("The Shawshank Redemption",1994, 5
                ,listDirector, listCast, listGenre));

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", movieId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Shawshank Redemption"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(1994))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.directors[0].name").value("Frank Darabont"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.casts[0].name").value("Tim Robbins"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.casts[1].name").value("Morgan Freeman"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genres[0].name").value("Drama"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genres[1].name").value("Crime"));
    }
    @Test
    void getMovieNotFound() throws Exception {
        // Fictitious movie ID that does not exist
        Long movieId = 999L;

        // Configuring the mock service to throw an exception when the movie is requested
        when(moviesService.getMovie(movieId)).thenThrow(new AppException("Movie not found --> " + movieId, HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movie not found --> " + movieId));
    }
}