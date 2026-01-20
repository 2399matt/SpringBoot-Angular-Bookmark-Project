package com.example.bookmark.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record BookMarkRequest(@NotNull(message = "URL is required!") @URL(message = "URL must be valid.") String url,
                              List<Long> tagIds) {
}
