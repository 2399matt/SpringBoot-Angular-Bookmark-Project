package com.example.bookmark.dto;

import java.util.Date;

public record BookMarkDTO(Long id, String title, String url, String faviconUrl, String description, Date createdAt, int views) {
}
