package com.project.BookMyShowApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String bookingNumber;


    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="show_id",nullable = false)
    private Show show;

    @Column(nullable = false)
    private String status;//confirm,cancel,pending

    @Column(nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<ShowSeat> showSeats;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "Payment_id")
    private Payment payment;


}
