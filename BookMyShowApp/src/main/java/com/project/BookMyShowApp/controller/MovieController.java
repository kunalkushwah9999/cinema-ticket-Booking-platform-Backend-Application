package com.project.BookMyShowApp.controller;

import com.project.BookMyShowApp.dto.MovieDto;
import com.project.BookMyShowApp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/create")
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto){
        return new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id){
        return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(){
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieDto>> getMoviesByLanguage(@PathVariable String language){
        return new ResponseEntity<>(movieService.getMoviesByLanguage(language), HttpStatus.OK);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(@PathVariable String genre){
        return new ResponseEntity<>(movieService.getMoviesByGenre(genre), HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<MovieDto>> getMoviesByTitle(@PathVariable String title){
        return new ResponseEntity<>(movieService.searchMovies(title), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto){
        return new ResponseEntity<>(movieService.updateMovie(id, movieDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
    }

}