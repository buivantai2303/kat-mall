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

import java.util.List;
import java.util.Objects;

/**
 * Use Case: Get Banner
 * Retrieves banners by various criteria.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetBannerUseCase {

    private final BannerRepository bannerRepository;

    /**
     * Finds a banner by ID.
     * 
     * @param id The banner ID
     * @return The banner
     * @throws ResourceNotFoundException if not found
     */
    public Banner findById(String id) {
        Objects.requireNonNull(id, "Banner ID must not be null");
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner", id));
    }

    /**
     * Finds all active banners.
     * 
     * @return List of active banners
     */
    public List<Banner> findAllActive() {
        return bannerRepository.findAllActive();
    }

    /**
     * Finds active banners by position.
     * 
     * @param position The display position
     * @return List of banners at the position
     */
    public List<Banner> findByPosition(KTypeBannerPosition position) {
        Objects.requireNonNull(position, "Position must not be null");
        return bannerRepository.findActiveByPosition(position);
    }

    /**
     * Finds all banners with pagination.
     * 
     * @param page Page number
     * @param size Page size
     * @return List of banners
     */
    public List<Banner> findAll(int page, int size) {
        return bannerRepository.findAll(page, size);
    }
}
