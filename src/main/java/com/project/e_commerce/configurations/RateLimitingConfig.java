package com.project.e_commerce.configurations;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import org.springframework.context.annotation.Configuration;


import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitingConfig {

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ipAddress) {
        return ipBucketMap.computeIfAbsent(ipAddress, k -> createBucket());
    }

    private Bucket createBucket() {
        // Allow 5 requests initially and refill 1 token every 10 seconds
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(1, Duration.ofSeconds(10)));
        return Bucket.builder().addLimit(limit).build();
    }
} 