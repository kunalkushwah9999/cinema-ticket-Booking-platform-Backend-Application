package com.project.BookMyShowApp.repository;

import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowId(Long id);

    List<ShowSeat> findByShowIdAndStatus(Long id, String status);

    List<ShowSeat> findByBookingId(Long id);

}