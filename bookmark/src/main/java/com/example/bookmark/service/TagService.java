package com.example.bookmark.service;

import com.example.bookmark.dao.TagRepo;
import com.example.bookmark.entity.Tag;
import com.example.bookmark.exceptions.TagNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepo tagRepo;


    public TagService(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    /**
     * Finds all tags in the database.
     *
     * @return All present tags.
     */
    public List<Tag> findAll() {
        return tagRepo.findAll();
    }

    /**
     * Finds all tags whose id is present in the list.
     *
     * @param ids The list of id's to check for.
     * @return All tags with a matching id.
     */
    public List<Tag> findAllById(List<Long> ids) {
        return tagRepo.findAllById(ids);
    }

    /**
     * Finds a tag by a given id value.
     *
     * @param id The id for the tag to search for.
     * @return The tag.
     */
    public Tag findById(Long id) {
        return tagRepo.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag with id: " + id + " not found!"));
    }
}
