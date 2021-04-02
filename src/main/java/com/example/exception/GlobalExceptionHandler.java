package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorModel> handleRollbackException(RollbackException ex) {
        StringBuilder builder;
        if (ex.getCause() instanceof ConstraintViolationException) {
            builder = new StringBuilder("Field constraint violation:");
            ConstraintViolationException exCause = (ConstraintViolationException) ex.getCause();
            exCause.getConstraintViolations().stream().forEach(cs -> builder.append(" " + cs.getPropertyPath() + " " +
                    cs.getMessage() + ";"));
            return handleException(builder.toString(), exCause, HttpStatus.BAD_REQUEST);
        } else {
            return handleException(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorModel> handleEntityExistsException(EntityExistsException ex) {
        return handleException(ex.getMessage(), ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorModel> handleNoResultException(NoResultException ex) {
        return handleException(ex.getMessage(), ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ApiErrorModel> handleException(String message, Exception ex, HttpStatus httpStatus) {
        log.error(message, ex);
        ApiErrorModel apiErrorModel = new ApiErrorModel(httpStatus, message);
        return new ResponseEntity<>(apiErrorModel, httpStatus);
    }
}
