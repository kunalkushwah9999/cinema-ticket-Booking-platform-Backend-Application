package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.dto.TheaterDto;
import com.project.BookMyShowApp.exception.ResourceNotFoundException;
import com.project.BookMyShowApp.model.Theater;
import com.project.BookMyShowApp.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public TheaterDto createTheater(TheaterDto theaterDto){
        Theater theater = mapToEntity(theaterDto);
        Theater savedTheater = theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    public TheaterDto getTheaterById(Long id){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Found with id : " + id));
        return mapToDto(theater);
    }

    public List<TheaterDto> getAllTheaters(){
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TheaterDto> getTheatersByCity(String city){
        List<Theater> theaters = theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater doesn't exist"));
        theaterRepository.delete(theater);
    }

    public TheaterDto updateTheater(Long id, TheaterDto theaterDto) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater doesn't exist"));
        theater.setName(theaterDto.getName());
        theater.setCity(theaterDto.getCity());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreens(theaterDto.getTotalScreens());
        Theater savedTheater = theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    public TheaterDto mapToDto(Theater theater){
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setCity(theater.getCity());
        theaterDto.setTotalScreens(theater.getTotalScreens());
        return theaterDto;
    }

    public Theater mapToEntity(TheaterDto theaterDto){
        Theater theater = new Theater();
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreens(theaterDto.getTotalScreens());
        theater.setCity(theaterDto.getCity());
        return theater;
    }


}