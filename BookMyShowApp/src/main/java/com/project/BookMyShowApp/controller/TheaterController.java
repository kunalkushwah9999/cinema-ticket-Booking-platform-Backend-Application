package com.project.BookMyShowApp.controller;

import com.project.BookMyShowApp.dto.TheaterDto;
import com.project.BookMyShowApp.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping("/create")
    public ResponseEntity<TheaterDto> createTheater(@RequestBody TheaterDto theaterDto){
        return new ResponseEntity<>(theaterService.createTheater(theaterDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TheaterDto> getTheaterById(@PathVariable Long id){
        return new ResponseEntity<>(theaterService.getTheaterById(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<TheaterDto>> getAllTheaters(){
        return new ResponseEntity<>(theaterService.getAllTheaters(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TheaterDto> updateTheater(@PathVariable Long id,@RequestBody TheaterDto theaterDto){
        return new ResponseEntity<>(theaterService.updateTheater(id, theaterDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTheater(@PathVariable Long id){
        theaterService.deleteTheaterById(id);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDto>> getTheatersByCity(@PathVariable String city){
        return new ResponseEntity<>(theaterService.getTheatersByCity(city), HttpStatus.OK);
    }
}