package com.example.bookmark.dto;

import com.example.bookmark.entity.BookMark;
import com.example.bookmark.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {

    public TagDTO tagToDto(Tag tag) {
        return new TagDTO(tag.getId(), tag.getName());
    }

    public BookMarkDTO bookMarkToDto(BookMark bookMark) {
        return new BookMarkDTO(bookMark.getId(), bookMark.getTitle(), bookMark.getUrl(), bookMark.getFaviconUrl(),
                bookMark.getDescription(), bookMark.getCreatedAt(), bookMark.getViews());
    }

    public List<TagDTO> tagsToDtos(List<Tag> tags) {
        return tags.stream().map(tag -> tagToDto(tag)).toList();
    }

    public List<BookMarkDTO> bookMarksToDtos(List<BookMark> bookMarks) {
        return bookMarks.stream().map(bookMark -> bookMarkToDto(bookMark)).toList();
    }
}
