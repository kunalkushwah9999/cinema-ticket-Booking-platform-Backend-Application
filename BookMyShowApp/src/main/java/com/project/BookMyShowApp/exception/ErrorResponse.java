package com.project.BookMyShowApp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Date timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
}