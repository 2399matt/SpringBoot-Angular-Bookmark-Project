package com.example.bookmark.dao;

import com.example.bookmark.entity.BookMark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepo extends JpaRepository<BookMark, Long> {

    Page<BookMark> findByUserIdAndTagsId(String userId, Long id, Pageable pageable);

    Page<BookMark> findByUserIdAndTagsName(String userId, String name, Pageable pageable);

    Page<BookMark> findByUserIdAndTagsIdIn(String userId, List<Long> ids, Pageable pageable);

    @Query("SELECT DISTINCT b.id FROM BookMark b WHERE b.userId=:userId")
    Page<Long> findAllIds(@Param("userId") String userId, Pageable pageable);

    @EntityGraph(attributePaths = "tags")
    @Query("SELECT b from BookMark b WHERE b.id IN :idList")
    List<BookMark> findByIdWithTags(@Param("idList") List<Long> idList);

    @EntityGraph(attributePaths = "tags")
    @Query("SELECT b FROM BookMark b WHERE b.id=:id AND b.userId=:uid")
    Optional<BookMark> findByIdWithTags(@Param("id") Long id, @Param("uid") String uid);

    @Modifying
    @Query("UPDATE BookMark b SET b.views = b.views + 1 WHERE b.id=:id AND b.userId=:userId")
    void incrementViews(@Param("id") Long id, @Param("userId") String userId);

    @EntityGraph(attributePaths = "tags")
    @Query("SELECT b FROM BookMark b WHERE b.title LIKE(CONCAT('%', :term, '%')) AND b.userId=:userId")
    Page<BookMark> findByTerm(@Param("term") String term, @Param("userId") String userId, Pageable pageable);

    @EntityGraph(attributePaths = "tags")
    @Query("SELECT DISTINCT b FROM BookMark b JOIN b.tags t WHERE b.title LIKE(CONCAT('%', :term, '%')) AND b.userId=:userId AND t.id IN :tagIds")
    Page<BookMark> findByTermWithTags(@Param("term") String term, @Param("userId") String userId, Pageable pageable, @Param("tagIds") List<Long> tagIds);
}
