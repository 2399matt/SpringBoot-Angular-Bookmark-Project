package com.example.bookmark.exceptions;

import java.util.Date;

public record ErrorResponse(String message, Date timestamp) {
}
