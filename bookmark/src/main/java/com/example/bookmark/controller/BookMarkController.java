package com.example.bookmark.controller;

import com.example.bookmark.dto.BookMarkDTO;
import com.example.bookmark.dto.BookMarkRequest;
import com.example.bookmark.dto.BookMarkUpdateRequest;
import com.example.bookmark.dto.DtoMapper;
import com.example.bookmark.entity.BookMark;
import com.example.bookmark.entity.Tag;
import com.example.bookmark.service.BookMarkHelper;
import com.example.bookmark.service.BookMarkService;
import com.example.bookmark.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/bookmarks")
public class BookMarkController {

    private final BookMarkService bookMarkService;
    private final TagService tagService;
    private final BookMarkHelper bookMarkHelper;
    private final DtoMapper dtoMapper;

    public BookMarkController(BookMarkService bookMarkService, TagService tagService, BookMarkHelper bookMarkHelper, DtoMapper dtoMapper) {
        this.bookMarkService = bookMarkService;
        this.tagService = tagService;
        this.bookMarkHelper = bookMarkHelper;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookMarkDTO> findById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        BookMark bookMark = bookMarkService.findByIdWithTags(id, jwt.getSubject());
        return ResponseEntity.ok(dtoMapper.bookMarkToDto(bookMark));
    }

    @GetMapping
    public ResponseEntity<Page<BookMarkDTO>> findAll(@AuthenticationPrincipal Jwt jwt, @RequestParam(name = "tagIds", required = false) List<Long> tagIds,
                                                     Pageable pageable, @RequestParam(name = "searchTerm", required = false) String searchTerm) {
        Page<BookMark> bookmarks;
        if (tagIds == null || tagIds.isEmpty()) {
            if (searchTerm != null && !searchTerm.isEmpty()) {
                bookmarks = bookMarkService.findByTerm(searchTerm, pageable, jwt.getSubject());
            } else {
                bookmarks = bookMarkService.findAllWithTags(jwt.getSubject(), pageable);
            }
        } else {
            if (searchTerm != null && !searchTerm.isEmpty()) {
                bookmarks = bookMarkService.findByTermWithTags(searchTerm, jwt.getSubject(), pageable, tagIds);
            } else {
                bookmarks = bookMarkService.findByTagsIdIn(tagIds, jwt.getSubject(), pageable);
            }
        }
        Page<BookMarkDTO> dtoPage = bookmarks.map(bookMark -> dtoMapper.bookMarkToDto(bookMark));
        return ResponseEntity.ok(dtoPage);
    }

    @PutMapping
    public ResponseEntity<BookMarkDTO> updateBookMark(@RequestBody BookMarkUpdateRequest request, @AuthenticationPrincipal Jwt jwt) {
        BookMark bookMark = bookMarkService.findByIdWithTags(request.id(), jwt.getSubject());
        bookMark.setTitle(request.title());
        bookMark.setDescription(request.description());
        return ResponseEntity.ok(dtoMapper.bookMarkToDto(bookMarkService.save(bookMark)));
    }


    @PutMapping("/views/{id}")
    public ResponseEntity<Void> incrementViews(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        bookMarkService.incrementViews(id, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BookMarkDTO> createBookMark(@RequestBody BookMarkRequest request, @AuthenticationPrincipal Jwt jwt) {
        BookMark bookMark = bookMarkHelper.createBookMark(request.url());
        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagService.findAllById(request.tagIds()));
            bookMark.setTags(tags);
        }
        bookMark.setUserId(jwt.getSubject());
        BookMarkDTO bookMarkDto = dtoMapper.bookMarkToDto(bookMarkService.save(bookMark));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMarkDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookMark(@PathVariable Long id) {
        bookMarkService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
