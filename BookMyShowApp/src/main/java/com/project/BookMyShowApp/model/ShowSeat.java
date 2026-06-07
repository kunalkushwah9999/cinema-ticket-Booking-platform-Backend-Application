//package com.project.BookMyShowApp.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name="show_seat")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//
//public class ShowSeat {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Version
//    private Integer version;
//
//    @ManyToOne
//    @JoinColumn(name="show_id",nullable = false)
//    private Show show;
//
//    @ManyToOne
//    @JoinColumn(name="seat_id",nullable=false)
//    private Seat seat;
//
//    @Column(nullable = false)
//    private String status;
//
//    @Column(nullable = false)
//    private Double price;
//
//    @ManyToOne
//    @JoinColumn(name="booking_id")
//    private Booking booking;
//}
//
package com.project.BookMyShowApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_seat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
