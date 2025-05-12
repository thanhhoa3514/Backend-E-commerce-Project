package com.project.e_commerce.components;


import com.project.e_commerce.security.CustomOAuth2User;
import com.project.e_commerce.services.jwt.IJwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final IJwtService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 authentication success for user: {}", authentication.getName());
        log.info("Authentication class: {}", authentication.getClass().getName());
        log.info("Authentication details: {}", authentication.getDetails());

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            log.info("OAuth2 user attributes: {}", oauth2User.getAttributes());
            log.info("OAuth2 user authorities: {}", oauth2User.getAuthorities());
            log.info("OAuth2 user name: {}", oauth2User.getName());
        } else {
            log.warn("Principal is not an OAuth2User but: {}", authentication.getPrincipal().getClass().getName());
        }

        // Store authentication in session
        HttpSession session = request.getSession(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        log.info("Stored authentication in session and security context");

        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("Redirecting to: {}", targetUrl);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String targetUrl = "/api/v1/auth/success"; // Redirect to our success endpoint

        // Generate token for different types of principals
        if (authentication.getPrincipal() instanceof CustomOAuth2User userPrincipal) {
            log.info("Principal is CustomOAuth2User");
            String token = jwtService.generateAccessToken(userPrincipal);
            log.info("Generated token for CustomOAuth2User");

            // We don't need to add token as query param since we'll get it from the success endpoint
            return targetUrl;
        } else if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            log.info("Principal is OAuth2User");
            String token = jwtService.generateAccessToken(oauth2User);
            log.info("Generated token for OAuth2User");

            return targetUrl;
        }

        log.warn("Unknown principal type: {}", authentication.getPrincipal().getClass().getName());
        return targetUrl;
    }
}
