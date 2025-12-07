/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application properties configuration.
 * Binds to 'app.*' properties.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    /** Application name */
    private String name = "KatMall";

    /** Application version */
    private String version = "1.0.0";

    /** Base URL for backend API */
    private String baseUrl = "http://localhost:8080";

    /** Frontend URL for links in emails */
    private String frontendUrl = "http://localhost:3000";
}
