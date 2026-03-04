package com.project.BookMyShowApp.dto;

import com.project.BookMyShowApp.model.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterDto {

    private Long id;
    private String name;
    private String address;
    private String city;
    private Integer totalScreens;

}