package com.books.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookExceptionHandler {
    // Customized exception handler when the book is not found
    @ExceptionHandler
    public ResponseEntity<BookErrorResponse> handleException(BookNotFoundException exception) {
        BookErrorResponse bookErrorResponse = new BookErrorResponse(
                HttpStatus.NOT_FOUND.value(), exception.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.NOT_FOUND);
    }

    // Customized exception for any other request
    @ExceptionHandler
    public ResponseEntity<BookErrorResponse> handleException(Exception exception) {
        BookErrorResponse bookErrorResponse = new BookErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Invalid request", System.currentTimeMillis()
        );
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
