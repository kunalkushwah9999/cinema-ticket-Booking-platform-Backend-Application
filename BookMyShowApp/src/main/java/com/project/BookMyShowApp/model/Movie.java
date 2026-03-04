package com.project.BookMyShowApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;
    private String language;
    private String genre;
    private Integer durationMins;
    private LocalDateTime releaseDate;
    private String posterUrl;
    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    private List<Show> Shows;






}
