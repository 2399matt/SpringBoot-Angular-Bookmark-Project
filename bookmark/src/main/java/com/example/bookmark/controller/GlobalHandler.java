package com.example.bookmark.controller;

import com.example.bookmark.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalHandler {


    private final Logger logger = LoggerFactory.getLogger(GlobalHandler.class);

    @ExceptionHandler(BookMarkNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookMarkNotFoundException(BookMarkNotFoundException e) {
        logger.error("Bookmark not found! Error: {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(TagNotFoundException e) {
        logger.error("Tag not found! Error: {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BookMarkCreationException.class)
    public ResponseEntity<ErrorResponse> handleBookMarkCreationException(BookMarkCreationException e) {
        logger.error("BookMark creation failed! Error: {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ArgsErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> details = e.getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        ArgsErrorResponse response = new ArgsErrorResponse(details, new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
