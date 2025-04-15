package com.project.e_commerce.integration.controllers;

import com.project.e_commerce.dtos.user.UserProfileDTO;
import com.project.e_commerce.integration.BaseIntegrationTest;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.models.user.UserProfile;
import com.project.e_commerce.repositories.UserProfileRepository;
import com.project.e_commerce.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProfileControllerTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private User testUser;
    private UserProfile testUserProfile;

    @BeforeEach
    void setUp() {
        // Clean up existing data
//        userProfileRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");

        testUser = userRepository.save(testUser);

        // Create test user profile
        testUserProfile = UserProfile.builder()
                .user(testUser)
                .bio("Test bio")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .profilePicture("profile.jpg")
                .build();
        testUserProfile = userProfileRepository.save(testUserProfile);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("Should get current user profile")
    void shouldGetCurrentUserProfile() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio", is("Test bio")))
                .andExpect(jsonPath("$.profilePicture", is("profile.jpg")));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    @DisplayName("Should update current user profile")
    void shouldUpdateCurrentUserProfile() throws Exception {
        // Arrange
        UserProfileDTO updateDTO = UserProfileDTO.builder()
                .bio("Updated bio")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .profilePicture("updated-profile.jpg")
                .build();

        // Act
        ResultActions result = mockMvc.perform(put("/api/v1/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.bio", is("Updated bio")))
                .andExpect(jsonPath("$.profilePicture", is("updated-profile.jpg")));
    }
}