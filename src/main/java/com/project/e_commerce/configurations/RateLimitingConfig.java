package com.project.e_commerce.configurations;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitingConfig {

    @Bean
    public Map<String, Bucket> buckets() {
        return new ConcurrentHashMap<>();
    }

    public Bucket resolveBucket(String key) {
        return buckets().computeIfAbsent(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1))))
                .build();
    }
}