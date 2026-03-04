package com.project.BookMyShowApp.controller;

import com.project.BookMyShowApp.dto.PaymentVerficationRequest;
import com.project.BookMyShowApp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

//    @PostMapping("/create-payment")
//    public ResponseEntity<String> createOrder(@RequestBody PaymentDto paymentDto){
//        try{
//            String service = paymentService.createPayment(paymentDto);
//            return ResponseEntity.ok(service);
//        }
//        catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error in creating order");
//        }
//    }

    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerficationRequest request) {
        try{
            paymentService.verifyPayment(request);
            return ResponseEntity.ok("Payment Successful");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error in creating order");
        }
    }
}