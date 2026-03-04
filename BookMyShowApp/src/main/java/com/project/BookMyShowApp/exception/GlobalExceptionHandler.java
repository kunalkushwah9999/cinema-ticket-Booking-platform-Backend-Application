package com.project.BookMyShowApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.security.sasl.AuthenticationException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                exception.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeatNotAvailableException.class)
    public ResponseEntity<?> seatNotAvailableException(SeatNotAvailableException exception, WebRequest webRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "Seat Unavailable",
                exception.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid email or password");
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception exception, WebRequest webRequest){
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server Error",
                exception.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}