package com.project.e_commerce.filters;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.services.token.TokenBlacklistServiceImpl;
import com.project.e_commerce.services.jwt.IJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final IJwtService jwtService;
    private final UserRepository userRepository;
    private final TokenBlacklistServiceImpl tokenBlacklistServiceImpl;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Skip OAuth2 endpoints
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/oauth2/") || requestURI.contains("/login/oauth2/") ||
            requestURI.contains("/api/v1/login/oauth2/") || requestURI.contains("/api/v1/auth/success")) {
            log.info("Skipping JWT filter for OAuth2 endpoint: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (tokenBlacklistServiceImpl.isTokenBlacklisted(token)) {
                    log.warn("Blacklisted token used: {}", token);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token has been invalidated");
                    return;
                }

                    if (jwtService.validateToken(token)&& !jwtService.isTokenExpired(token)) {
                        String phoneNumber = jwtService.getUsernameFromToken(token);
                        String role = jwtService.getRoleFromToken(token);

                        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            User user = userRepository.findByPhoneNumber(phoneNumber)
                                    .orElseThrow(() -> new RuntimeException("User not found"));

                            List<GrantedAuthority> authorities = new ArrayList<>();
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    user, null, authorities);
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    } else {
                        log.error("Invalid JWT token");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
