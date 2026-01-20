package com.example.bookmark.service;

import com.example.bookmark.dao.BookMarkRepo;
import com.example.bookmark.entity.BookMark;
import com.example.bookmark.exceptions.BookMarkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookMarkService {

    private final Logger logger = LoggerFactory.getLogger(BookMarkService.class);

    private final BookMarkRepo bookMarkRepo;

    public BookMarkService(BookMarkRepo bookMarkRepo) {
        this.bookMarkRepo = bookMarkRepo;
    }

    public Page<BookMark> findByTagsId(Long id, String userId, Pageable pageable) {
        return bookMarkRepo.findByUserIdAndTagsId(userId, id, pageable);
    }

    public Page<BookMark> findByTagsName(String name, String userId, Pageable pageable) {
        return bookMarkRepo.findByUserIdAndTagsName(userId, name, pageable);
    }

    /**
     * Finds all bookmark objects associated with the current user id.
     *
     * @param userId   The id of the user.
     * @param pageable
     * @return Page of BookMarks.
     */
    public Page<BookMark> findAllWithTags(String userId, Pageable pageable) {
        // Testing out the two-query approach rather than join to avoid the in-memory paging.
        Page<Long> idList = bookMarkRepo.findAllIds(userId, pageable);
        if (idList.isEmpty()) {
            return Page.empty(pageable);
        }
        List<BookMark> bookmarks = bookMarkRepo.findByIdWithTags(idList.getContent());
        return new PageImpl<>(bookmarks, pageable, idList.getTotalElements());
    }

    /**
     * Finds all bookmark objects based on the user id and a list of tags.
     *
     * @param ids      List of tag id's to match.
     * @param userId   The id of the user.
     * @param pageable
     * @return Page of BookMarks.
     */
    public Page<BookMark> findByTagsIdIn(List<Long> ids, String userId, Pageable pageable) {
        return bookMarkRepo.findByUserIdAndTagsIdIn(userId, ids, pageable);
    }

    /**
     * Finds all bookmark objects based on a term. Pattern matches the bookmark's title.
     *
     * @param term     The term to search for.
     * @param pageable
     * @param userId   The id of the user.
     * @return Page of BookMarks.
     */
    public Page<BookMark> findByTerm(String term, Pageable pageable, String userId) {
        return bookMarkRepo.findByTerm(term, userId, pageable);
    }

    /**
     * Finds all bookmark objects based on a search term and list of tag id's.
     *
     * @param term     The term to search for.
     * @param userId   The id of the user.
     * @param pageable
     * @param tagIds   The id of the active tags.
     * @return Page of BookMarks.
     */
    public Page<BookMark> findByTermWithTags(String term, String userId, Pageable pageable, List<Long> tagIds) {
        return bookMarkRepo.findByTermWithTags(term, userId, pageable, tagIds);
    }

    /**
     * Finds a single bookmark object based on id and eagerly loads the associated tags.
     *
     * @param id     The id of the bookmark object.
     * @param userId The id of the user.
     * @return The BookMark object.
     */
    public BookMark findByIdWithTags(Long id, String userId) {
        return bookMarkRepo.findByIdWithTags(id, userId)
                .orElseThrow(() -> new BookMarkNotFoundException("Bookmark with id: " + id + " not found!"));
    }

    public BookMark findById(Long id) {
        return bookMarkRepo.findById(id)
                .orElseThrow(() -> new BookMarkNotFoundException("bookmark with id: " + id + " not found!"));
    }

    /**
     * Increments the view count on a bookmark object. Uses an update statement in a transaction.
     *
     * @param id     The id of the bookmark object to update.
     * @param userId The id of the user.
     */
    @Transactional
    public void incrementViews(Long id, String userId) {
        logger.info("Incrementing view count for bookmark with id: {}", id);
        bookMarkRepo.incrementViews(id, userId);
    }

    /**
     * Saves a bookmark object to the database.
     *
     * @param bookMark The bookmark object to be saved.
     * @return The persisted bookmark object.
     */
    @Transactional
    public BookMark save(BookMark bookMark) {
        logger.info("Saving bookmark {}", bookMark.getTitle());
        return bookMarkRepo.save(bookMark);
    }

    /**
     * Deletes a bookmark object from the database.
     *
     * @param bookMark The bookmark object to delete.
     */
    @Transactional
    public void delete(BookMark bookMark) {
        bookMarkRepo.delete(bookMark);
    }

    /**
     * Deletes a bookmark object by id.
     *
     * @param id The id of the bookmark object to delete.
     */
    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting bookmark with id: {}", id);
        bookMarkRepo.deleteById(id);
    }
}
