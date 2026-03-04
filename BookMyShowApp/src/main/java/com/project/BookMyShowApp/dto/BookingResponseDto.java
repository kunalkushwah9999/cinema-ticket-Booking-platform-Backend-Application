package com.project.BookMyShowApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    // Your application's internal ID for the booking
    private Long bookingId;

    // The Razorpay order ID (e.g., "order_JvPj9s0j3K8q0X")
    //private String razorpayOrderId;

    // The total amount in the smallest currency unit (e.g., 450.50 becomes 45050)
    private Double amountInPaise;

    // Your public Razorpay Key ID (e.g., "rzp_test_12345...")
    //private String razorpayKeyId;
}