/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.cms.application.usecase;

import com.en.katmall.co.cms.domain.repository.BannerRepository;
import com.en.katmall.co.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Use Case: Delete Banner
 * Deletes a banner from CMS.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteBannerUseCase {

    private final BannerRepository bannerRepository;

    /**
     * Executes the delete banner use case.
     * 
     * @param bannerId The banner ID
     * @throws ResourceNotFoundException if not found
     */
    public void execute(String bannerId) {
        Objects.requireNonNull(bannerId, "Banner ID must not be null");
        if (bannerRepository.findById(bannerId).isEmpty()) {
            throw new ResourceNotFoundException("Banner", bannerId);
        }
        bannerRepository.deleteById(bannerId);
    }
}
