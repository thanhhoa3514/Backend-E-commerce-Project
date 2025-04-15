package com.project.e_commerce.filters;

import com.project.e_commerce.configurations.RateLimitingConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitingConfig rateLimitingConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (isAuthEndpoint(requestURI)) {
            String clientIP = getClientIP(request);
            Bucket bucket = rateLimitingConfig.resolveBucket(clientIP);

            if (bucket.tryConsume(1)) {
                // Request allowed, proceed with the filter chain
                filterChain.doFilter(request, response);
            } else {
                // Rate limit exceeded
                log.warn("Rate limit exceeded for IP: {}", clientIP);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests. Please try again later.");
            }
        } else {
            // Not an auth endpoint, proceed normally
            filterChain.doFilter(request, response);
        }
    }

    private boolean isAuthEndpoint(String uri) {
        return uri.contains("/api/v1/users/login") ||
                uri.contains("/api/v1/users/register") ||
                uri.contains("/api/v1/auth/refresh-token");
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
