package com.example.bookmark.service;

import com.example.bookmark.entity.BookMark;
import com.example.bookmark.exceptions.BookMarkCreationException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class BookMarkHelper {

    private Logger logger = LoggerFactory.getLogger(BookMarkHelper.class);

    /**
     * Creates the foundation of a bookmark object. Uses Jsoup api to parse the given uri for
     * attributes and sets the favicon url.
     *
     * @param url The page to create the bookmark for.
     * @return The bookmark object for the given url.
     */
    public BookMark createBookMark(String url) {
        logger.info("Creating book mark for {}", url);
        URI uri = URI.create(url);
        if (uri.getScheme() == null) {
            uri = URI.create("https://" + url);
            logger.info("URI created: {}", uri);
        }
        try {
            Document document = Jsoup.connect(uri.toString()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:119.0) Gecko/20100101 Firefox/119.0")
                    .timeout(5000)
                    .referrer("https://www.google.com")
                    .get();
            String title = getTitle(document);
            String description = getDescription(document);
            String faviconUrl = getFaviconUrl(uri.toString());
            BookMark bookMark = new BookMark();
            bookMark.setTitle(title);
            bookMark.setDescription(description);
            bookMark.setUrl(uri.toString());
            bookMark.setFaviconUrl(faviconUrl);
            logger.info("BookMark instantiated. Title: {}, Description: {}", title, description);
            return bookMark;
        }catch(HttpStatusException e) {
            logger.warn("Site ({}) blocked access! Creating default bookmark", uri);
            BookMark bookMark = new BookMark();
            bookMark.setUrl(uri.toString());
            bookMark.setTitle(uri.getHost());
            bookMark.setDescription("Set Description!");
            bookMark.setFaviconUrl(getFaviconUrl(uri.toString()));
            return bookMark;
        }
        catch (IOException e) {
            logger.warn("I/O Error {}, falling back!", e.getMessage());
            throw new BookMarkCreationException("Failure during document parsing!");
        } catch (Exception e) {
            logger.warn("Non I/O Error {}, falling back!", e.getMessage());
            throw new BookMarkCreationException("Failure during document creation!");
        }
    }

    /**
     * Parses the document to retrieve the title for the bookmark
     *
     * @param document The document to parse.
     * @return The title for the bookmark.
     */
    private String getTitle(Document document) {
        Element ogTitle = document.selectFirst("meta[property=og:title]");
        if (ogTitle != null) {
            return ogTitle.attr("content").trim();
        }
        return document.title().trim();
    }

    /**
     * Parses the document to retrieve the description for the bookmark.
     *
     * @param document The document to parse.
     * @return The description for the bookmark.
     */
    private String getDescription(Document document) {
        Element ogDescription = document.selectFirst("meta[property=og:description]");
        if (ogDescription != null) {
            return limitDescription(ogDescription.attr("content"));
        }
        Element metaDescription = document.selectFirst("meta[name=description]");
        if (metaDescription != null) {
            return limitDescription(metaDescription.attr("content"));
        }
        logger.warn("No description found for {}", document.title());
        return "";
    }

    /**
     * Limits the character length of the description for larger descriptions.
     *
     * @param metaDesc The description to trim.
     * @return The trimmed description for the bookmark.
     */
    private String limitDescription(String metaDesc) {
        if (metaDesc.length() > 500) {
            metaDesc = metaDesc.trim().substring(0, 499);
        }
        return metaDesc;
    }

    /**
     * Uses Google's favicon API to store a link to the url.
     *
     * @param url The url from the user.
     * @return The url for the favicon.
     */
    private String getFaviconUrl(String url) {
        return "https://www.google.com/s2/favicons?sz=64&domain=" + url;
    }
}
