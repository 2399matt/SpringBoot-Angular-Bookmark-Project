package com.example.bookmark.exceptions;

public class BookMarkNotFoundException extends RuntimeException {
    public BookMarkNotFoundException(String message) {
        super(message);
    }
}
