package com.project.BookMyShowApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="shows")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name="screen_id",nullable = false)
    private Screen screen;

    @ManyToOne
    @JoinColumn(name="movie_id",nullable = false)
    private Movie movie;

    @OneToMany(mappedBy = "show",cascade = CascadeType.ALL)
    private List<ShowSeat> showSeats;

    @OneToMany(mappedBy = "show",cascade = CascadeType.ALL)
    private List<Booking> bookings;


}
