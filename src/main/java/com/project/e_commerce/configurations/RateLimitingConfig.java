package com.project.e_commerce.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitingConfig {
    @Bean
    public Map<String, Bucket> ipBucketMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Bucket createNewBucket() {
        // Allow 10 login attempts per minute
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
