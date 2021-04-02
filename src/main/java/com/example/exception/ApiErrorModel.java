package com.example.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ApiErrorModel {

    private String error;

    private int status;

    private LocalDateTime timestamp;

    private String message;

    public ApiErrorModel(HttpStatus httpStatus, String message) {
        this.error = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.timestamp = LocalDateTime.now(ZoneOffset.UTC);
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
