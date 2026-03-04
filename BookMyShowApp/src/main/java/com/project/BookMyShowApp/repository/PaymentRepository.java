package com.project.BookMyShowApp.repository;

import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String id);
}