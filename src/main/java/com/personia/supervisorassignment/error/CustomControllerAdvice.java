package com.personia.supervisorassignment.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
class CustomControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NullPointerException.class) // exception handled
    public ResponseEntity<Object> handleNullPointerExceptions(Exception e) {
        // ... potential custom logic

        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        return buildResponseEntity(new ApiError(status, e.getMessage(), e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(final Exception e) {
        // ... potential custom logic

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        return buildResponseEntity(new ApiError(status, e.getMessage(), e));
    }

    @ExceptionHandler(CycleDetectedException.class)
    public ResponseEntity<Object> handleCycleExceptions(final Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiError(status, e.getMessage(), e));
    }

    @ExceptionHandler(MultipleRootsDetectedException.class)
    public ResponseEntity<Object> handleMultipleRootsExceptions(final Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildResponseEntity(new ApiError(status, e.getMessage(), e));
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getCause()instanceof InvalidFormatException) {
            return buildResponseEntity(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "UNPROCESSABLE_ENTITY", ex));
        } else {
            return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex));
        }
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}