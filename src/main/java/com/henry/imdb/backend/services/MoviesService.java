package com.henry.imdb.backend.services;

import com.henry.imdb.backend.domain.dtos.MovieDetailDto;
import com.henry.imdb.backend.domain.dtos.MovieDto;
import com.henry.imdb.backend.domain.exceptions.AppException;
import com.henry.imdb.backend.domain.mappers.MovieMapper;
import com.henry.imdb.backend.domain.models.Cast;
import com.henry.imdb.backend.domain.models.Director;
import com.henry.imdb.backend.domain.models.Genre;
import com.henry.imdb.backend.domain.models.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final MovieMapper movieMapper;
    private final List<Director> directors1 = List.of(
            new Director(1L, "Frank Darabont"));
    private final List<Director> directors2 = List.of(
            new Director(2L, "Francis Ford Coppola"));
    private final List<Director> directors3 = List.of(
            new Director(3L, "Christopher Nolan"));
    private final List<Director> directors4 = List.of(
            new Director(4L, "Robert Zemeckis"));
    private final List<Cast> casts1 = List.of(
            new Cast(1L, "Tim Robbins")
            , new Cast(2L, "Morgan Freeman"));
    private final List<Cast> casts2 = List.of(
            new Cast(1L, "Tim Robbins")
            , new Cast(2L, "Al Pacino")
            , new Cast(3L, "James Caan"));
    private final List<Cast> casts3 = List.of(
            new Cast(4L, "Leonardo DiCaprio")
            , new Cast(5L, "Joseph Gordon-Levitt")
            , new Cast(6L, "Ellen Page"));
    private final List<Cast> casts4 = List.of(
            new Cast(7L, "Tom Hanks")
            , new Cast(8L, "Robin Wright")
            , new Cast(8L, " Gary Sinise"));

    private final List<Genre> genres1 = List.of(
            new Genre(1L, "Drama")
            , new Genre(2L, "Crime"));
    private final List<Genre> genres2 = List.of(
            new Genre(1L, "Drama")
            , new Genre(2L, "Crime"));
    private final List<Genre> genres3 = List.of(
            new Genre(3L, "Action")
            , new Genre(4L, "Adventure")
            , new Genre(5L, "Sci-Fi"));
    private final List<Genre> genres4 = List.of(
            new Genre(1L, "Drama")
            , new Genre(6L, "Romance")
            , new Genre(7L, "Comedy"));
    private final List<Movie> movies = List.of(
            new Movie(1L, "The Shawshank Redemption"
                    , "The film follows the story of Andy Dufresne, " +
                    "a banker wrongly convicted of murder, " +
                    "as he tries to survive in Shawshank prison. " +
                    "Through his friendship with fellow inmate Red, " +
                    "Andy finds hope and redemption in an apparently hopeless place."
                    , 1994, 5, directors1, casts1, genres1),
            new Movie(2L, "The Godfather"
                    , "The Godfather narrates the story of " +
                    "the powerful Corleone mafia family and its leader, " +
                    "Don Vito Corleone, as they navigate the world of " +
                    "organized crime in New York. The film explores themes of loyalty, " +
                    "betrayal, and the cost of power."
                    , 1972, 5, directors2, casts2, genres2),
            new Movie(3L, "Inception"
                    , "Inception follows Dom Cobb, a skilled mind thief who steals secrets " +
                    "from the subconscious while people dream. When he's offered an" +
                    " apparently impossible task of implanting an idea into " +
                    "someone's mind (inception), he embarks on a journey of " +
                    "intrigue and altered reality"
                    , 2010, 4, directors3, casts3, genres3),
            new Movie(4L, "Forrest Gump"
                    , "The film follows the life of Forrest Gump, " +
                    " man with intellectual disabilities who lives an" +
                    " extraordinary life. Across decades of American history, " +
                    "Forrest finds himself in unusual situations while impacting " +
                    "the lives of those around him.", 1995, 4, directors4, casts4, genres4));

    public List<MovieDto> allMovies() {
        List<MovieDto> moviesDto = new ArrayList<>();
        for (Movie movie : movies) {
            moviesDto.add(movieMapper.toMovieDto(movie));

        }
        return moviesDto;
    }

    public MovieDetailDto getMovie(Long id) {
        MovieDetailDto movieDetailDto = null;
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                movieDetailDto = movieMapper.toMovieDetailDto(movie);
                break;
            }
        }
        if (movieDetailDto == null) {
            throw new AppException("Movie not found --> " + id, HttpStatus.NOT_FOUND);
        }
        return movieDetailDto;
    }
}