/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.application.usecase;

import com.en.katmall.co.cms.domain.model.News;
import com.en.katmall.co.cms.domain.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Create News
 * Creates a new news article.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateNewsUseCase {

    private final NewsRepository newsRepository;

    /**
     * Executes the create news use case.
     * 
     * @param slug         URL-friendly identifier
     * @param thumbnailUrl Thumbnail image URL
     * @param authorId     Author's administrator ID
     * @return Created news article
     */
    public News execute(String slug, String thumbnailUrl, String authorId) {
        Objects.requireNonNull(slug, "slug must not be null");

        if (newsRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug already exists: " + slug);
        }

        News news = News.builder()
                .slug(slug)
                .thumbnailUrl(thumbnailUrl)
                .authorId(authorId)
                .isPublished(false)
                .build();

        return newsRepository.save(news);
    }
}
