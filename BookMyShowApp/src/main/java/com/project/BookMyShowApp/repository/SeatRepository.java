package com.project.BookMyShowApp.repository;

import com.project.BookMyShowApp.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}