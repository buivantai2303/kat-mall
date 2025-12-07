/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.event;

import com.en.katmall.co.shared.domain.DomainEvent;
import lombok.Getter;

/**
 * Domain event raised when a user account is locked.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public class UserLockedEvent extends DomainEvent {

    private final String userId;
    private final String reason;

    /**
     * Creates a new user locked event
     * 
     * @param userId The ID of the locked user
     * @param reason The reason for locking
     */
    public UserLockedEvent(String userId, String reason) {
        super();
        this.userId = userId;
        this.reason = reason;
    }

    @Override
    public String getEventType() {
        return "USER_LOCKED";
    }
}
