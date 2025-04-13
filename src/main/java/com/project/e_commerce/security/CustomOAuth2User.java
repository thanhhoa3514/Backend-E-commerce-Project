package com.project.e_commerce.security;

import com.project.e_commerce.models.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class CustomOAuth2User implements OAuth2User {

    private Long id;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomOAuth2User(Long id, String email, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public static CustomOAuth2User create(User user, Map<String, Object> attributes) {
        if (user == null || attributes == null) {
            throw new IllegalArgumentException("User and attributes cannot be null");
        }

        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        return new CustomOAuth2User(
                user.getId(),
                user.getEmail(),
                authorities,
                attributes
        );
    }


    @Override
    public String getName() {
        return email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
