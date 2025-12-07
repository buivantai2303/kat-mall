/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.application.usecase;

import com.en.katmall.co.cms.domain.model.Banner;
import com.en.katmall.co.cms.domain.repository.BannerRepository;
import com.en.katmall.co.shared.enums.KTypeBannerPosition;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Update Banner
 * Updates banner content, position, and activation status.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateBannerUseCase {

    private final BannerRepository bannerRepository;

    /**
     * Updates banner content.
     * 
     * @param bannerId  The banner ID
     * @param name      New name
     * @param imageUrl  New image URL
     * @param targetUrl New target URL
     * @return Updated banner
     * @throws ResourceNotFoundException if not found
     */
    public Banner updateContent(String bannerId, String name, String imageUrl, String targetUrl) {
        Objects.requireNonNull(bannerId, "Banner ID must not be null");
        Banner banner = findById(bannerId);
        banner.updateContent(name, imageUrl, targetUrl);
        return bannerRepository.save(banner);
    }

    /**
     * Updates banner position and priority.
     * 
     * @param bannerId The banner ID
     * @param position New position
     * @param priority New priority
     * @return Updated banner
     * @throws ResourceNotFoundException if not found
     */
    public Banner updatePositionAndPriority(String bannerId, KTypeBannerPosition position, int priority) {
        Objects.requireNonNull(bannerId, "Banner ID must not be null");
        Banner banner = findById(bannerId);
        banner.updatePosition(position);
        banner.updatePriority(priority);
        return bannerRepository.save(banner);
    }

    /**
     * Activates a banner.
     * 
     * @param bannerId The banner ID
     * @return Activated banner
     * @throws ResourceNotFoundException if not found
     */
    public Banner activate(String bannerId) {
        Objects.requireNonNull(bannerId, "Banner ID must not be null");
        Banner banner = findById(bannerId);
        banner.activate();
        return bannerRepository.save(banner);
    }

    /**
     * Deactivates a banner.
     * 
     * @param bannerId The banner ID
     * @return Deactivated banner
     * @throws ResourceNotFoundException if not found
     */
    public Banner deactivate(String bannerId) {
        Objects.requireNonNull(bannerId, "Banner ID must not be null");
        Banner banner = findById(bannerId);
        banner.deactivate();
        return bannerRepository.save(banner);
    }

    /**
     * Finds a banner by ID.
     * 
     * @param id Banner ID
     * @return The banner
     * @throws ResourceNotFoundException if not found
     */
    private Banner findById(String id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner", id));
    }
}
