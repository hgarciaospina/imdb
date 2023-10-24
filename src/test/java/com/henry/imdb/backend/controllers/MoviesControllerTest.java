package com.henry.imdb.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.henry.imdb.backend.ImdbApplication;
import com.henry.imdb.backend.config.RestExceptionHandler;
import com.henry.imdb.backend.domain.dtos.*;
import com.henry.imdb.backend.domain.exceptions.AppException;
import com.henry.imdb.backend.domain.models.Cast;
import com.henry.imdb.backend.domain.models.Director;
import com.henry.imdb.backend.domain.models.Genre;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ImdbApplication.class)
class MoviesControllerTest {
    @MockBean
    private MoviesService moviesService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Assign a MovieController instance
        MoviesController moviesController = new MoviesController(moviesService);

        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        // Configure MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(moviesController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    void allMovies() throws Exception {
        when(moviesService.allMovies()).thenReturn(Arrays.asList(
                new MovieDto("The Shawshank Redemption", 1984, 5),
                new MovieDto("The Godfather", 1975, 5)
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
    void getMovie() throws Exception {
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
                (new MovieDetailDto(
                        "The Shawshank Redemption",
                        "The film follows the story of Andy Dufresne, " +
                                "a banker wrongly convicted of murder, " +
                                "as he tries to survive in Shawshank prison. " +
                                "Through his friendship with fellow inmate Red, " +
                                "Andy finds hope and redemption in an apparently hopeless place.",
                        1994,
                        5,
                        listDirector,
                        listCast,
                        listGenre));

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", movieId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("The Shawshank Redemption"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.synopsis").value("The film follows the story of Andy Dufresne, " +
                        "a banker wrongly convicted of murder, " +
                        "as he tries to survive in Shawshank prison. " +
                        "Through his friendship with fellow inmate Red, " +
                        "Andy finds hope and redemption in an apparently hopeless place."))
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

    @Test
    void updateMovie() throws Exception {
        // Create a sample movieUpdateDto
        MovieUpdateDto movieUpdateDto = new MovieUpdateDto();
        List<GenreDto> genresUpdateDto = new ArrayList<>();
        genresUpdateDto.add(new GenreDto("Drama-Update"));
        genresUpdateDto.add(new GenreDto("Crime-Update"));
        movieUpdateDto.setSynopsis("Updated synopsis");
        movieUpdateDto.setRating(4);
        movieUpdateDto.setGenres(genresUpdateDto);

        // Fictitious movie ID
        Long movieId = 999L;

        // Configure the mock service to return a modified movie
        when(moviesService.updateMovie(movieId, movieUpdateDto)).thenReturn(movieUpdateDto);

        // Convert the DTO to JSON
        byte[] movieUpdateDtoJson = objectMapper.writeValueAsBytes(movieUpdateDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movieUpdateDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateMovieNotFound() throws Exception {
        // Fictitious movie ID that does not exist
        Long movieId = 999L;
        // Configuring the mock service to throw an exception when the movie is requested
        when(moviesService.updateMovie(eq(movieId), any(MovieUpdateDto.class))).thenThrow(new AppException("Movie not found --> " + movieId, HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.patch("/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createMovie() throws Exception {
        // Create a sample movieCreateDto
        MovieCreateDto movieCreateDto = MovieCreateDto.builder()
                .id(10L)
                .title("The Best Seller")
                .synopsis("Across decades of American history" +
                        " Forrest finds himself")
                .releaseYear(2023)
                .rating(5)
                .directors(List.of(
                        new Director(20L, "Pedro Picapiedra"),
                        new Director(21L, " Pablo Mármol")))
                .casts(List.of(
                        new Cast(30L, "Tony Curtis"),
                        new Cast(31L, "Roger Moore")))
                .genres(List.of(
                        new Genre(10L, "Drama"),
                        new Genre(11L, "Funny")))

                .build();

        // Configure movieService behavior to return the same object
        when(moviesService.createMovie(any(MovieCreateDto.class)))
                .thenReturn(movieCreateDto);

        // Make the mock POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 10, " +
                                "\"title\": \"The Best Seller\"," +
                                "\"synopsis\": \"Across decades of American history Forrest finds himself\"," +
                                "\"releaseYear\": 2023," +
                                "\"rating\": 5," +
                                "\"directors\": [{\"id\": 20, \"name\": \"Pedro Picapiedra\"}, {\"id\": 21, \"name\": \"Pablo Mármol\"}], " +
                                "\"casts\": [{\"id\": 30, \"name\": \"Tony Curtis\"}, {\"id\": 31, \"name\": \"Roger Moore\"}], " +
                                "\"genders\": [{\"id\": 10, \"name\": \"Drama\"}, {\"id\": 11, \"name\": \"Funny\"}]}"
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
        // Verify that the movieService.createMovie method was called with the correct object
        verify(moviesService, times(1)).createMovie(movieCreateDto);
    }
}