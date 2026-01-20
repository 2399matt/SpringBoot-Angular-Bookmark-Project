package com.example.bookmark.dto;

import jakarta.validation.constraints.NotNull;

public record BookMarkUpdateRequest(@NotNull(message = "id is required.") Long id,
                                    @NotNull(message = "title is required.") String title,
                                    @NotNull(message = "description is required.") String description) {
}
