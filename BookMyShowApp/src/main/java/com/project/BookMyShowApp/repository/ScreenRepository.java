package com.project.BookMyShowApp.repository;

import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
}