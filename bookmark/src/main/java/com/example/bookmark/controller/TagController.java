package com.example.bookmark.controller;

import com.example.bookmark.dto.DtoMapper;
import com.example.bookmark.dto.TagDTO;
import com.example.bookmark.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final DtoMapper dtoMapper;

    public TagController(TagService tagService, DtoMapper dtoMapper) {
        this.tagService = tagService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(dtoMapper.tagsToDtos(tagService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTag(@PathVariable Long id) {
        return ResponseEntity.ok(dtoMapper.tagToDto(tagService.findById(id)));
    }
}
