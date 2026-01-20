package com.example.bookmark.dao;

import com.example.bookmark.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Long> {

}
