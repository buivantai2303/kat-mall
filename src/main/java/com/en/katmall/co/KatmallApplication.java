/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the KatMall E-commerce application.
 * Initializes Spring Boot context and starts the application server.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@SpringBootApplication
public class KatmallApplication {

    /**
     * Application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(KatmallApplication.class, args);
    }
}
