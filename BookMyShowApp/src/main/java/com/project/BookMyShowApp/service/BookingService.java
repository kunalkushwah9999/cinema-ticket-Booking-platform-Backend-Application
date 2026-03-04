package com.project.BookMyShowApp.service;

import com.project.BookMyShowApp.dto.*;
import com.project.BookMyShowApp.exception.ResourceNotFoundException;
import com.project.BookMyShowApp.exception.SeatNotAvailableException;
import com.project.BookMyShowApp.model.*;
import com.project.BookMyShowApp.repository.BookingRepository;
import com.project.BookMyShowApp.repository.ShowRepository;
import com.project.BookMyShowApp.repository.ShowSeatRepository;
import com.project.BookMyShowApp.repository.UserRepository;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

//    @Autowired
//    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;


    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) throws RazorpayException {
        System.out.println("In Booking Service");
        User user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Show show = showRepository.findById(bookingRequestDto.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));

        List<ShowSeat> showSeatList = showSeatRepository.findAllById(bookingRequestDto.getSeatIds());

        for(ShowSeat showSeat : showSeatList){
            if(!"AVAILABLE".equals(showSeat.getStatus())){
                throw new SeatNotAvailableException("Seat " + showSeat.getSeat().getId() + "is not available");
            }
            showSeat.setStatus("LOCKED");
        }

        showSeatRepository.saveAll(showSeatList);

        Double totalAmount = showSeatList.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("PENDING");
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setTotalAmount(totalAmount);

//        String razorpayOrderString = paymentService.createOrder(totalAmount);
//
//        JSONObject razorpayOrder = new JSONObject(razorpayOrderString);
//        String razorpayOrderId = razorpayOrder.getString("id");
//        String receiptId = razorpayOrder.getString("receipt");
//
        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setStatus("CREATE");
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentMethod(bookingRequestDto.getPaymentMethod());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setRazorpayPaymentId("RazorpayPaymentId");
        payment.setRazorpaySignature("RazorpaySignature");
        payment.setRazorpayOrderId("RazorpayOrderId");
        payment.setStatus("SUCCESS");

        booking.setPayment(payment);
        payment.setBooking(booking);

        Booking savedBooking = bookingRepository.save(booking);

        for(ShowSeat seat : showSeatList){
            seat.setBooking(savedBooking);
        }

        showSeatRepository.saveAll(showSeatList);

        System.out.println("Calling Email Service");
        emailService.sendSuccessfulEmail(user.getEmail(), booking.getId(), showSeatList);

        return new BookingResponseDto(
                savedBooking.getId(),
                //razorpayOrderId,
                totalAmount
                // "RAZORPAY_KEY_ID" // Get this from @Value
        );

    }

    public BookingDto getBookingById(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found"));

        List<ShowSeat> showSeatList = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, showSeatList);

    }

    public BookingDto getBookingByBookingNumber(String bookingNumber){
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found"));

        List<ShowSeat> showSeatList = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, showSeatList);

    }

    public List<BookingDto> getBookingByUserId(Long id){
        List<Booking> bookings = bookingRepository.findByUserId(id);
        return bookings.stream()
                .map(booking -> {
                    List<ShowSeat> seats = showSeatRepository.findAll()
                            .stream()
                            .filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                            .collect(Collectors.toList());
                    return mapToBookingDto(booking, seats);
                })
                .collect(Collectors.toList());

    }

    @Transactional
    public BookingDto cancelBooking(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Booking Not found"));

        booking.setStatus("CANCELLED");

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat -> {
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });

//        if(booking.getPayment()!=null){
//            booking.getPayment().setStatus("REFUNDED");
//        }

        Booking updatedBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);
        return mapToBookingDto(updatedBooking, seats);
    }

    BookingDto mapToBookingDto(Booking booking, List<ShowSeat> showSeats){
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        userDto.setEmail(booking.getUser().getEmail());
        bookingDto.setUser((userDto));

        ShowDto showDto = new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());

        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());
        screenDto.setName(booking.getShow().getScreen().getName());

        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setTotalScreens(booking.getShow().getScreen().getTheater().getTotalScreens());

        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);

        List<ShowSeatDto> seatDtos = showSeats.stream()
                .map(seat ->{
                    ShowSeatDto showSeatDto = new ShowSeatDto();
                    showSeatDto.setId(seat.getId());
                    showSeatDto.setStatus(seat.getStatus());
                    showSeatDto.setPrice(seat.getPrice());

                    SeatDto seatDto = new SeatDto();
                    seatDto.setId(seat.getSeat().getId());
                    seatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    seatDto.setSeatType(seat.getSeat().getSeatType());
                    seatDto.setBasePrice(seat.getSeat().getBasePrice());
                    showSeatDto.setSeat(seatDto);
                    return showSeatDto;
                })
                .collect(Collectors.toList());

        bookingDto.setShowSeats(seatDtos);

//        if(booking.getPayment()!=null){
//            PaymentDto paymentDto = new PaymentDto();
//            paymentDto.setId(booking.getPayment().getId());
//            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
//            paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
//            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
//            paymentDto.setAmount(booking.getPayment().getAmount());
//            paymentDto.setStatus(booking.getPayment().getStatus());
//            bookingDto.setPayment(paymentDto);
//        }

        return bookingDto;
    }
}