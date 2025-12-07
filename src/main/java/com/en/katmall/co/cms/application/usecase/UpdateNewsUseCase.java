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

import java.util.Objects;

/**
 * Use Case: Update News
 * Updates news articles including translations and publishing status.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateNewsUseCase {

    private final NewsRepository newsRepository;

    /**
     * Adds a translation to a news article.
     * 
     * @param newsId       The news ID
     * @param languageCode Language code (e.g., 'vi', 'en')
     * @param title        Title in the specified language
     * @param content      Content in the specified language
     * @return Updated news article
     * @throws ResourceNotFoundException if not found
     */
    public News addTranslation(String newsId, String languageCode, String title, String content) {
        Objects.requireNonNull(newsId, "News ID must not be null");
        News news = findById(newsId);
        news.addTranslation(languageCode, title, content);
        return newsRepository.save(news);
    }

    /**
     * Publishes a news article.
     * 
     * @param newsId The news ID
     * @return Published news article
     * @throws ResourceNotFoundException if not found
     */
    public News publish(String newsId) {
        Objects.requireNonNull(newsId, "News ID must not be null");
        News news = findById(newsId);
        news.publish();
        return newsRepository.save(news);
    }

    /**
     * Unpublishes a news article.
     * 
     * @param newsId The news ID
     * @return Unpublished news article
     * @throws ResourceNotFoundException if not found
     */
    public News unpublish(String newsId) {
        Objects.requireNonNull(newsId, "News ID must not be null");
        News news = findById(newsId);
        news.unpublish();
        return newsRepository.save(news);
    }

    /**
     * Finds a news article by ID.
     * 
     * @param id News ID
     * @return The news article
     * @throws ResourceNotFoundException if not found
     */
    private News findById(String id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", id));
    }
}
