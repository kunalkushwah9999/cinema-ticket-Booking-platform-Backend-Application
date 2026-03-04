package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.dto.MovieDto;
import com.project.BookMyShowApp.exception.ResourceNotFoundException;
import com.project.BookMyShowApp.model.Movie;
import com.project.BookMyShowApp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto){
        Movie movie = mapToMovie(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return mapToMovieDto(savedMovie);
    }

    public MovieDto getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));
        return mapToMovieDto(movie);
    }

    public List<MovieDto> getAllMovies(){
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToMovieDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMoviesByLanguage(String language){
        List<Movie> movies = movieRepository.findByLanguage(language);
        return movies.stream()
                .map(this::mapToMovieDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMoviesByGenre(String genre){
        List<Movie> movies = movieRepository.findByGenre(genre);
        return movies.stream()
                .map(this::mapToMovieDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> searchMovies(String title){
        List<Movie> movies = movieRepository.findByTitle(title);
        return movies.stream()
                .map(this::mapToMovieDto)
                .collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));
        movie.setTitle(movieDto.getTitle());
        movie.setGenre(movieDto.getGenre());
        movie.setDescription(movieDto.getDescription());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setLanguage(movieDto.getLanguage());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setReleaseDate(movieDto.getReleaseDate());

        Movie updatedMovie = movieRepository.save(movie);
        return mapToMovieDto(updatedMovie);
    }

    public void deleteMovie(Long id){
        Movie deleteMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));
        movieRepository.delete(deleteMovie);

    }

    public Movie mapToMovie(MovieDto movieDto){
        Movie movie = new Movie();
        movie.setDescription(movieDto.getDescription());
        movie.setGenre(movieDto.getGenre());
        movie.setLanguage(movieDto.getLanguage());
        movie.setTitle(movieDto.getTitle());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setReleaseDate(movieDto.getReleaseDate());
        return movie;
    }

    public MovieDto mapToMovieDto(Movie movie){
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDescription(movie.getDescription());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDurationMins(movie.getDurationMins());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }


}