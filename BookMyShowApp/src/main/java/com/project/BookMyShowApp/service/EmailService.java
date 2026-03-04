package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.Seat;
import com.project.BookMyShowApp.model.ShowSeat;
import com.project.BookMyShowApp.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BookingRepository bookingRepository;

    public void sendSuccessfulEmail(String toEmail, Long bookingId, List<ShowSeat> list){
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        String bookingNo = booking.get().getBookingNumber();
        StringBuilder bookedSeats = new StringBuilder();
        for(ShowSeat s : list){
            Seat seat = s.getSeat();
            bookedSeats.append(seat.getSeatNumber());
            bookedSeats.append(" ");
            bookedSeats.append(seat.getSeatType());
            bookedSeats.append("\n");

        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Booking Successful");
        message.setText("Hello there! \n Your Booking has done with booking Number " + bookingNo + ". \n" + "Your booked seats are "+ bookedSeats);

        javaMailSender.send(message);
    }

    public void sendFailedEmail(String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Booking Failed");
        message.setText("There might be some issue in verification. Please check the information you entered and try again");
        javaMailSender.send(message);
    }



}