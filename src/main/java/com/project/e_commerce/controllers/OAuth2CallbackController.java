package com.project.e_commerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1/login/oauth2/code")
@RequiredArgsConstructor
@Slf4j
public class OAuth2CallbackController {

    @GetMapping("/google")
    public RedirectView googleCallback(HttpServletRequest request) {
        // Get authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken)) {

            log.info("OAuth2 callback received with authenticated user: {}", authentication.getName());

            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                log.info("OAuth2 user attributes: {}", oauth2User.getAttributes());
                log.info("OAuth2 user authorities: {}", oauth2User.getAuthorities());
            }
        } else {
            log.warn("OAuth2 callback received but no valid authentication found");
            if (authentication != null) {
                log.info("Authentication type: {}", authentication.getClass().getName());
                log.info("Principal type: {}", authentication.getPrincipal().getClass().getName());
                log.info("Is authenticated: {}", authentication.isAuthenticated());
            }
        }

        // Ensure session is created and authentication is stored
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // Redirect to the success URL
        return new RedirectView("/api/v1/auth/success");
    }
}
