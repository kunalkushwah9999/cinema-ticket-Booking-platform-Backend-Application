package com.project.BookMyShowApp.controller;

import com.project.BookMyShowApp.dto.ShowDto;
import com.project.BookMyShowApp.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/show")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping("/create")
    public ResponseEntity<ShowDto> createShow(@RequestBody ShowDto showDto){
        return new ResponseEntity<>(showService.createShow(showDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id){
        return new ResponseEntity<>(showService.getShowById(id), HttpStatus.OK);
    }

    @GetMapping          // might be too broad. Consider restricting it to filters (by movie, city, date) for scalability.
    public ResponseEntity<List<ShowDto>> getAllShows(){
        return new ResponseEntity<>(showService.getAllShows(), HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDto>> getShowsByMovie(@PathVariable Long movieId){
        return new ResponseEntity<>(showService.getShowsByMovie(movieId), HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}/city/{city}")
    public ResponseEntity<List<ShowDto>> getShowsByCity(@PathVariable Long movieId, @PathVariable String city){
        return new ResponseEntity<>(showService.getShowsByMovieAndCity(movieId, city), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ShowDto> updateShow(@PathVariable Long id, @RequestBody ShowDto showDto){
        return new ResponseEntity<>(showService.updateShow(id,showDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteShow(@PathVariable Long id){
        showService.deleteShow(id);
    }

    @GetMapping("/city/{city}/date/{date}")
    public ResponseEntity<List<ShowDto>> getShowsByMovie(
            @PathVariable String city,
            @PathVariable LocalDate date){
        return new ResponseEntity<>(showService.getShowsByCityAndDate(city, date), HttpStatus.OK);
    }
}