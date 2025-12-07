/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.application.usecase;

import com.en.katmall.co.cms.domain.model.News;
import com.en.katmall.co.cms.domain.repository.NewsRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Use Case: Get News
 * Retrieves news articles by various criteria.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetNewsUseCase {

    private final NewsRepository newsRepository;

    /**
     * Finds a news article by ID.
     * 
     * @param id The news ID
     * @return The news article
     * @throws ResourceNotFoundException if not found
     */
    public News findById(String id) {
        Objects.requireNonNull(id, "News ID must not be null");
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", id));
    }

    /**
     * Finds a news article by slug.
     * 
     * @param slug The URL-friendly identifier
     * @return The news article
     * @throws ResourceNotFoundException if not found
     */
    public News findBySlug(String slug) {
        Objects.requireNonNull(slug, "Slug must not be null");
        return newsRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("News", "slug", slug));
    }

    /**
     * Finds all published news articles.
     * 
     * @return List of published news
     */
    public List<News> findAllPublished() {
        return newsRepository.findAllPublished();
    }

    /**
     * Finds news articles by author.
     * 
     * @param authorId The author's ID
     * @return List of news by the author
     */
    public List<News> findByAuthor(String authorId) {
        Objects.requireNonNull(authorId, "Author ID must not be null");
        return newsRepository.findByAuthorId(authorId);
    }

    /**
     * Finds all news articles with pagination.
     * 
     * @param page Page number
     * @param size Page size
     * @return List of news
     */
    public List<News> findAll(int page, int size) {
        return newsRepository.findAll(page, size);
    }
}
