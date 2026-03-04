package com.project.BookMyShowApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerficationRequest {
    // The order ID you got from Razorpay (e.g., "order_JvPj9s0j3K8q0X")
    private String razorpayOrderId;

    // The payment ID Razorpay generates (e.g., "pay_JvPj9s0j3K8q0Y")
    private String razorpayPaymentId;

    // The HMAC signature Razorpay generates to prove the request is valid
    private String razorpaySignature;
}