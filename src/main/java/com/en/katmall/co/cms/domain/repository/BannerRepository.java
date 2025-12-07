/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.domain.repository;

import com.en.katmall.co.cms.domain.model.Banner;
import com.en.katmall.co.shared.enums.KTypeBannerPosition;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Banner aggregate root.
 * Defines the contract for banner persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface BannerRepository {

    /**
     * Saves a banner
     * 
     * @param banner The banner to save
     * @return The saved banner
     */
    Banner save(Banner banner);

    /**
     * Finds banner by ID
     * 
     * @param id The banner ID
     * @return Optional containing the banner if found
     */
    Optional<Banner> findById(String id);

    /**
     * Finds all active banners
     * 
     * @return List of active banners
     */
    List<Banner> findAllActive();

    /**
     * Finds active banners by display position
     * 
     * @param position The display position
     * @return List of banners at the specified position, ordered by priority
     */
    List<Banner> findActiveByPosition(KTypeBannerPosition position);

    /**
     * Finds all banners (paginated)
     * 
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return List of banners
     */
    List<Banner> findAll(int page, int size);

    /**
     * Deletes a banner by ID
     * 
     * @param id The banner ID to delete
     */
    void deleteById(String id);
}
