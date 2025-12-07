/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.application.usecase;

import com.en.katmall.co.cms.domain.model.Banner;
import com.en.katmall.co.cms.domain.repository.BannerRepository;
import com.en.katmall.co.shared.enums.KTypeBannerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Create Banner
 * Creates a new banner for CMS.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateBannerUseCase {

    private final BannerRepository bannerRepository;

    /**
     * Executes the create banner use case.
     * 
     * @param name      Banner name
     * @param imageUrl  Banner image URL
     * @param targetUrl Target link URL
     * @param position  Display position
     * @param priority  Display priority
     * @return Created banner
     */
    public Banner execute(String name, String imageUrl, String targetUrl,
            KTypeBannerPosition position, int priority) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(imageUrl, "imageUrl must not be null");

        Banner banner = Banner.builder()
                .name(name)
                .imageUrl(imageUrl)
                .targetUrl(targetUrl)
                .displayPosition(position)
                .priority(priority)
                .isActive(true)
                .build();

        return bannerRepository.save(banner);
    }
}
