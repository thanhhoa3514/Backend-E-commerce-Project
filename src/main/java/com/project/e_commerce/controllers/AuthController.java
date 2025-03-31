package com.project.e_commerce.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {


    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken() {
        // The SecurityContextHolder will only have an authentication if the token is valid
        // Spring Security's JWT filter will have already validated the token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Boolean> response = new HashMap<>();
        boolean isValid = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
}
