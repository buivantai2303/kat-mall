/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.application.usecase;

import com.en.katmall.co.cms.domain.repository.NewsRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Delete News
 * Deletes a news article.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteNewsUseCase {

    private final NewsRepository newsRepository;

    /**
     * Executes the delete news use case.
     * 
     * @param newsId The news ID
     * @throws ResourceNotFoundException if not found
     */
    public void execute(String newsId) {
        Objects.requireNonNull(newsId, "News ID must not be null");
        if (newsRepository.findById(newsId).isEmpty()) {
            throw new ResourceNotFoundException("News", newsId);
        }
        newsRepository.deleteById(newsId);
    }
}
