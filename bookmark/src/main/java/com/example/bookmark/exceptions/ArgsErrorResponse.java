package com.example.bookmark.exceptions;

import java.util.Date;
import java.util.List;

public record ArgsErrorResponse(List<String> details, Date timestamp) {
}
