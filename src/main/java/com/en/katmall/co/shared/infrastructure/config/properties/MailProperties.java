/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Mail service configuration properties.
 * Binds to 'mail.*' properties.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {

    /** Sender email address */
    private String from = "noreply@katmall.vn";

    /** Sender display name */
    private String fromName = "KatMall";

    /** Whether email sending is enabled */
    private boolean enabled = true;
}
