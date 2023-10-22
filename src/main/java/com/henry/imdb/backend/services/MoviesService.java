package com.henry.imdb.backend.services;

import com.henry.imdb.backend.domain.dtos.MovieCreateDto;
import com.henry.imdb.backend.domain.dtos.MovieDetailDto;
import com.henry.imdb.backend.domain.dtos.MovieDto;
import com.henry.imdb.backend.domain.dtos.MovieUpdateDto;
import com.henry.imdb.backend.domain.exceptions.AppException;
import com.henry.imdb.backend.domain.mappers.GenreMapper;
import com.henry.imdb.backend.domain.mappers.MovieMapper;
import com.henry.imdb.backend.domain.models.Cast;
import com.henry.imdb.backend.domain.models.Director;
import com.henry.imdb.backend.domain.models.Genre;
import com.henry.imdb.backend.domain.models.Movie;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service
public class MoviesService {

    private final MovieMapper movieMapper;
    private final GenreMapper genreMapper;
    private List<Director> directors1 = new ArrayList<>();
    private List<Director> directors2 = new ArrayList<>();
    private List<Director> directors3 = new ArrayList<>();
    private List<Director> directors4 = new ArrayList<>();
    private List<Cast> casts1 = new ArrayList<>();
    private List<Cast> casts2 = new ArrayList<>();
    private List<Cast> casts3 = new ArrayList<>();
    private List<Cast> casts4 = new ArrayList<>();
    private List<Genre> genres1 = new ArrayList<>();
    private List<Genre> genres2 = new ArrayList<>();
    private List<Genre> genres3 = new ArrayList<>();
    private List<Genre> genres4 = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    public MoviesService(MovieMapper movieMapper, GenreMapper genreMapper) {
        this.movieMapper = movieMapper;
        this.genreMapper = genreMapper;
        initializeData();
    }

    private void initializeData() {
        directors1.add(new Director(1L, "Frank Darabont"));
        directors2.add(new Director(2L, "Francis Ford Coppola"));
        directors3.add(new Director(3L, "Christopher Nolan"));
        directors4.add(new Director(4L, "Robert Zemeckis"));
        casts1.add(new Cast(1L, "Tim Robbins"));
        casts1.add(new Cast(2L, "Morgan Freeman"));
        casts2.add(new Cast(3L, "Marlon Brando"));
        casts2.add(new Cast(4L, "Al Pacino"));
        casts2.add(new Cast(5L, "James Caan"));
        casts3.add(new Cast(6L, "Leonardo DiCaprio"));
        casts3.add(new Cast(7L, "Joseph Gordon-Levitt"));
        casts3.add(new Cast(8L, "Ellen Page"));
        casts4.add(new Cast(9L, "Tom Hanks"));
        casts4.add(new Cast(10L, "Robin Wright"));
        casts4.add(new Cast(11L, " Gary Sinise"));
        genres1.add(new Genre(1L, "Drama"));
        genres1.add(new Genre(2L, "Crime"));
        genres2.add(new Genre(1L, "Drama"));
        genres2.add(new Genre(2L, "Crime"));
        genres3.add(new Genre(3L, "Action"));
        genres3.add(new Genre(4L, "Adventure"));
        genres3.add(new Genre(5L, "Sci-Fi"));
        genres4.add(new Genre(1L, "Drama"));
        genres4.add(new Genre(6L, "Romance"));
        genres4.add(new Genre(7L, "Comedy"));
        movies.add(new Movie(1L, "The Shawshank Redemption"
                , "The film follows the story of Andy Dufresne, " +
                "a banker wrongly convicted of murder, " +
                "as he tries to survive in Shawshank prison. " +
                "Through his friendship with fellow inmate Red, " +
                "Andy finds hope and redemption in an apparently hopeless place."
                , 1994, 5, directors1, casts1, genres1));
        movies.add(new Movie(2L, "The Godfather"
                , "The Godfather narrates the story of " +
                "the powerful Corleone mafia family and its leader, " +
                "Don Vito Corleone, as they navigate the world of " +
                "organized crime in New York. The film explores themes of loyalty, " +
                "betrayal, and the cost of power."
                , 1972, 5, directors2, casts2, genres2));
        movies.add(new Movie(3L, "Inception"
                , "Inception follows Dom Cobb, a skilled mind thief who steals secrets " +
                "from the subconscious while people dream. When he's offered an" +
                " apparently impossible task of implanting an idea into " +
                "someone's mind (inception), he embarks on a journey of " +
                "intrigue and altered reality"
                , 2010, 4, directors3, casts3, genres3));
        movies.add(new Movie(4L, "Forrest Gump"
                , "The film follows the life of Forrest Gump, " +
                " man with intellectual disabilities who lives an" +
                " extraordinary life. Across decades of American history, " +
                "Forrest finds himself in unusual situations while impacting " +
                "the lives of those around him.", 1995,
                4, directors4, casts4, genres4));
    }

    public List<MovieDto> allMovies() {
        List<MovieDto> moviesDto = new ArrayList<>();
        movies.forEach(movie -> moviesDto.add(movieMapper.toMovieDto(movie)));
        return moviesDto;
    }

    public MovieDetailDto getMovie(Long id) {
        Optional<MovieDetailDto> movieDetailDto = movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .map(movieMapper::toMovieDetailDto)
                .findFirst();

        return movieDetailDto.orElseThrow(() -> new AppException("Movie not found --> " + id, HttpStatus.NOT_FOUND));
    }

    public MovieUpdateDto updateMovie(Long id, MovieUpdateDto movieUpdateDto) {
        Movie movieToUpdate = movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AppException("Movie not found --> " + id, HttpStatus.NOT_FOUND));

        if (movieUpdateDto.getSynopsis() != null) {
            movieToUpdate.setSynopsis(movieUpdateDto.getSynopsis());
        }
        if (movieUpdateDto.getRating() != null) {
            movieToUpdate.setRating(movieUpdateDto.getRating());
        }
        if (movieUpdateDto.getGenres() != null) {

            movieToUpdate.setGenres(genreMapper.toGenreList(movieUpdateDto.getGenres()));
        }

        return movieMapper.toMovieUpdateDto(movieToUpdate);
    }

    public MovieCreateDto createMovie(MovieCreateDto movieCreateDto) {
        validateMovieData(movieCreateDto);
        movies.add(movieMapper.toMovie(movieCreateDto));

        return movieCreateDto;
    }

    private void validateMovieData(MovieCreateDto movieCreateDto) {
        if (movieCreateDto == null) {
            throw new AppException(" Movie data cannot be empty ", HttpStatus.BAD_REQUEST);
        }
        if (movieCreateDto.getTitle() == null)
            throw new AppException("The movie must have a title ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getTitle().isEmpty())
            throw new AppException("The movie must have a title ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getTitle().isBlank())
            throw new AppException("The movie must have a title ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getSynopsis() == null)
            throw new AppException("The movie must have a synopsis ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getSynopsis().isEmpty())
            throw new AppException("The movie must have a synopsis ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getSynopsis().isBlank())
            throw new AppException("The movie must have a synopsis ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getReleaseYear().toString().isBlank())
            throw new AppException("The movie must have a release year ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getRating().toString().isEmpty())
            throw new AppException("The movie must have a release year ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getDirectors().isEmpty())
            throw new AppException("At least one director of the film must enter ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getCasts().isEmpty())
            throw new AppException("At least one actor from the movie must enter ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getGenres().isEmpty())
            throw new AppException("You must enter at least one genre of the movie ", HttpStatus.BAD_REQUEST);
        if (movieCreateDto.getCasts().size() > 5)
            throw new AppException("You can only add up to 5 actors ", HttpStatus.BAD_REQUEST);
        if ((movieCreateDto.getRating() < 1) || (movieCreateDto.getRating() > 5))
            throw new AppException("The rating must be a number between 1 and 5. ", HttpStatus.BAD_REQUEST);

        searchMovieWithTitleAndYear(movieCreateDto);
    }

    private void searchMovieWithTitleAndYear(MovieCreateDto movieCreateDto) {
        movies.stream()
                .filter(movie -> movie.getTitle().equals(movieCreateDto.getTitle())
                        && movie.getReleaseYear().equals(movieCreateDto.getReleaseYear()))
                .findFirst()
                .ifPresent(movie -> {
                    throw new AppException(" There is already a movie with the title and year entered ", HttpStatus.BAD_REQUEST);
                });
    }
}