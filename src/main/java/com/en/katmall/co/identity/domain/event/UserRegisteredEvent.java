/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.identity.domain.event;

import com.en.katmall.co.shared.domain.DomainEvent;
import lombok.Getter;

/**
 * Domain event raised when a new user registers.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public class UserRegisteredEvent extends DomainEvent {

    private final String userId;
    private final String email;

    /**
     * Creates a new user registered event
     * 
     * @param userId The ID of the registered user
     * @param email  The email of the registered user
     */
    public UserRegisteredEvent(String userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }

    @Override
    public String getEventType() {
        return "USER_REGISTERED";
    }
}
