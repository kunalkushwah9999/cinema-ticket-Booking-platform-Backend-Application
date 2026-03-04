package com.project.BookMyShowApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="screens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    public String name;

    private Integer totalSeats;


    @ManyToOne
    @JoinColumn(name="theater_id",nullable = false)
    private Theater theater;

    @OneToMany(mappedBy = "screen" ,cascade = CascadeType.ALL)
    private List<Show> shows;

    @OneToMany(mappedBy ="screen" , cascade = CascadeType.ALL)
    private List<Seat> seats;
}
