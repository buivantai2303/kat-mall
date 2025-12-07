/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.domain.repository;

import com.en.katmall.co.cms.domain.model.News;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for News aggregate root.
 * Defines the contract for news persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface NewsRepository {

    /**
     * Saves a news article
     * 
     * @param news The news to save
     * @return The saved news
     */
    News save(News news);

    /**
     * Finds news by ID
     * 
     * @param id The news ID
     * @return Optional containing the news if found
     */
    Optional<News> findById(String id);

    /**
     * Finds news by slug
     * 
     * @param slug The URL-friendly identifier
     * @return Optional containing the news if found
     */
    Optional<News> findBySlug(String slug);

    /**
     * Finds all published news articles
     * 
     * @return List of published news
     */
    List<News> findAllPublished();

    /**
     * Finds news articles by author
     * 
     * @param authorId The author's administrator ID
     * @return List of news by the author
     */
    List<News> findByAuthorId(String authorId);

    /**
     * Finds all news articles (paginated)
     * 
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return List of news
     */
    List<News> findAll(int page, int size);

    /**
     * Deletes a news article by ID
     * 
     * @param id The news ID to delete
     */
    void deleteById(String id);

    /**
     * Checks if a slug is already taken
     * 
     * @param slug The slug to check
     * @return true if slug exists
     */
    boolean existsBySlug(String slug);
}
