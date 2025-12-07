/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.application.scheduler;

import com.en.katmall.co.identity.domain.repository.MemberRegistrationRepository;
import com.en.katmall.co.shared.infrastructure.config.properties.RegistrationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Scheduler for cleaning up expired member registrations.
 * Runs periodically to remove registrations that have exceeded the cleanup
 * period.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCleanupScheduler {

    private final MemberRegistrationRepository memberRegistrationRepository;
    private final RegistrationProperties registrationProperties;

    /**
     * Cleans up expired member registrations.
     * Runs every hour.
     */
    @Scheduled(fixedRate = 3600000) // Every hour
    public void cleanupExpiredRegistrations() {
        Instant cutoffTime = Instant.now()
                .minus(registrationProperties.getCleanupAfterHours(), ChronoUnit.HOURS);

        int deleted = memberRegistrationRepository.deleteExpiredBefore(cutoffTime);

        if (deleted > 0) {
            log.info("Cleaned up {} expired member registrations", deleted);
        }
    }
}
