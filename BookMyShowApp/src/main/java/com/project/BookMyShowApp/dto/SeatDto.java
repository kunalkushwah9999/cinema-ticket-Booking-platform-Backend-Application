package com.project.BookMyShowApp.dto;

import com.project.BookMyShowApp.model.Screen;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

    private Long id;
    private String seatNumber;
    private String seatType;
    private Double basePrice;
    //private ScreenDto screen;
}