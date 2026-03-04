
package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.dto.PaymentDto;
import com.project.BookMyShowApp.dto.PaymentVerficationRequest;
import com.project.BookMyShowApp.model.Booking;
import com.project.BookMyShowApp.model.Payment;
import com.project.BookMyShowApp.model.Seat;
import com.project.BookMyShowApp.model.ShowSeat;
import com.project.BookMyShowApp.repository.BookingRepository;
import com.project.BookMyShowApp.repository.PaymentRepository;
import com.project.BookMyShowApp.repository.SeatRepository;
import com.project.BookMyShowApp.repository.ShowSeatRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    public String createOrder(Double totalAmount) throws RazorpayException{
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject paymentRequest = new JSONObject();
        paymentRequest.put("amount", totalAmount * 100); // Use the secure amount passed in
        paymentRequest.put("currency", "INR");
        paymentRequest.put("receipt", "txn_" + UUID.randomUUID().toString());

        // Create the order on Razorpay's servers
        Order razorpayOrder = client.orders.create(paymentRequest);

        // Return the full order JSON to the BookingService
        return razorpayOrder.toString();
    }

//    public String createPayment(PaymentDto paymentDetails) throws RazorpayException{
//        RazorpayClient client = new RazorpayClient(keyId, keySecret);
//
//        Payment payment = dtoToModel(paymentDetails);
//
//        JSONObject paymentRequest = new JSONObject();
//        paymentRequest.put("amount", paymentDetails.getAmount()*100);
//        paymentRequest.put("currency", "INR");
//        paymentRequest.put("receipt", "txn_"+ UUID.randomUUID());
//
//        Order razorpayOrder = client.orders.create(paymentRequest);
//
//        System.out.println(razorpayOrder.toString());
//        //payment.setId(razorpayOrder.get("id"));
//        payment.setRazorpayOrderId(razorpayOrder.get("id"));
//        payment.setStatus("CREATED");
//        payment.setPaymentTime(LocalDateTime.now());
//        payment.setPaymentMethod("ONLINE");
//        payment.setTransactionId(razorpayOrder.get("receipt"));
//        payment.setAmount(razorpayOrder.get("amount"));
//
//        paymentRepository.save(payment);
//
//        return razorpayOrder.toString();
//
//    }



    @Transactional // <-- Make this transactional
    public void verifyPayment(PaymentVerficationRequest request) throws Exception {

        // 1. Find the payment record
        Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new Exception("Payment record not found for order: " + request.getRazorpayOrderId()));

        // 2. Verify the signature
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", request.getRazorpayOrderId());
        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
        options.put("razorpay_signature", request.getRazorpaySignature());

        boolean isSignatureValid = Utils.verifyPaymentSignature(options, this.keySecret);

        if (isSignatureValid) {
            // --- 3. PAYMENT IS SUCCESSFUL ---
            payment.setStatus("PAID");
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setPaymentTime(LocalDateTime.now());

            // --- 4. FIND AND UPDATE BOOKING ---
            Booking booking = payment.getBooking();
            booking.setStatus("CONFIRMED");

            // --- 5. FIND AND UPDATE SEATS ---
            List<ShowSeat> showSeatList = showSeatRepository.findByBookingId(booking.getId());
            Seat[] seatList = new Seat[showSeatList.size()];
            int i=0;
            for (ShowSeat seat : showSeatList) {
                seat.setStatus("BOOKED");
                seatList[i++] = seat.getSeat();
            }
            // --- 6. SAVE ALL CHANGES ---
            paymentRepository.save(payment);
            bookingRepository.save(booking);
            showSeatRepository.saveAll(showSeatList);

            emailService.sendSuccessfulEmail(booking.getUser().getEmail(), booking.getId(), showSeatList);


        } else {
            // --- 3. PAYMENT FAILED ---
            payment.setStatus("FAILED");
            Booking booking = payment.getBooking();
            booking.setStatus("FAILED");

            // --- 4. RELEASE THE SEATS ---
            List<ShowSeat> showSeatList = showSeatRepository.findByBookingId(booking.getId());
            for (ShowSeat seat : showSeatList) {
                seat.setStatus("AVAILABLE"); // Release the lock
                seat.setBooking(null);
            }

            paymentRepository.save(payment);
            bookingRepository.save(booking);
            showSeatRepository.saveAll(showSeatList);

            emailService.sendFailedEmail(payment.getBooking().getUser().getEmail());

            throw new Exception("Payment signature verification failed");
        }
    }
    public Payment dtoToModel(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setTransactionId(paymentDto.getTransactionId());
        payment.setPaymentTime(paymentDto.getPaymentTime());
        payment.setStatus(paymentDto.getStatus());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setAmount(paymentDto.getAmount());

        return payment;
    }

    public void updateOrderStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId).get();
        payment.setStatus(status);
        paymentRepository.save(payment);
    }


}
