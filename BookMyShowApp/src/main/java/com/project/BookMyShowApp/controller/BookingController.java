package com.project.BookMyShowApp.controller;

import com.project.BookMyShowApp.dto.BookingDto;
import com.project.BookMyShowApp.dto.BookingRequestDto;
import com.project.BookMyShowApp.dto.BookingResponseDto;
import com.project.BookMyShowApp.service.BookingService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequest) throws RazorpayException {
        System.out.println("In Booking Controller , Create Booking");
        return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id){
        return new ResponseEntity<>(bookingService.getBookingById(id), HttpStatus.OK);
    }

    @GetMapping("/bno/{bookingNumber}")
    public ResponseEntity<BookingDto> getBookingByBookingNumber(@PathVariable  String bookingNumber){
        return new ResponseEntity<>(bookingService.getBookingByBookingNumber(bookingNumber), HttpStatus.OK);
    }

    @GetMapping("/userId/{id}")
    public ResponseEntity<List<BookingDto>> getBookingByUserId(@PathVariable Long id){
        return new ResponseEntity<>(bookingService.getBookingByUserId(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable  Long id){
        return new ResponseEntity<>(bookingService.cancelBooking(id), HttpStatus.OK);
    }


}