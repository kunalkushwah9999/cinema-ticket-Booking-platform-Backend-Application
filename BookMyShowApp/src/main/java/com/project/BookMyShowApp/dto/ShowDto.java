package com.project.BookMyShowApp.dto;

import com.project.BookMyShowApp.model.Movie;
import com.project.BookMyShowApp.model.Screen;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowDto {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private MovieDto movie;
    private ScreenDto screen;
    private List<ShowSeatDto> availableSeats;

}