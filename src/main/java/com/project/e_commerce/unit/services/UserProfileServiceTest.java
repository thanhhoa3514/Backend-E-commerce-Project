package com.project.e_commerce.unit.services;

import com.project.e_commerce.models.user.User;
import com.project.e_commerce.models.user.UserProfile;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.unit.BaseUnitTest;

import java.time.LocalDate;

public class UserProfileServiceTest extends BaseUnitTest {

//    @Mock
//    private UserProfileRepository userProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IUserProfileMapperService userProfileMapperService;

    @InjectMocks
//    private UserProfileServiceImpl userProfileService;

    private User testUser;
    private UserProfile testUserProfile;
//    private UserProfileDTO testUserProfileDTO;


    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testUserProfile = UserProfile.builder()
                .id(1L)
                .user(testUser)
                .bio("Test bio")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .profilePicture("profile.jpg")
                .build();

        testUserProfileDTO = UserProfileDTO.builder()
                .id(1L)
                .userId(1L)
                .bio("Test bio")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .profilePicture("profile.jpg")
                .build();
    }

    @Test
    @DisplayName("Should get user profile by user ID")
    void shouldGetUserProfileByUserId() {
        // Arrange
        when(userProfileRepository.findByUserId(anyLong())).thenReturn(Optional.of(testUserProfile));
        when(userProfileMapperService.mapToDTO(any(UserProfile.class))).thenReturn(testUserProfileDTO);

        // Act
        UserProfileDTO result = userProfileService.getUserProfileByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUserProfileDTO.getId(), result.getId());
        assertEquals(testUserProfileDTO.getBio(), result.getBio());
    }

    @Test
    @DisplayName("Should update user profile")
    void shouldUpdateUserProfile() {
        // Arrange
        when(userProfileRepository.findByUserId(anyLong())).thenReturn(Optional.of(testUserProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapperService.mapToDTO(any(UserProfile.class))).thenReturn(testUserProfileDTO);

        // Act
        UserProfileDTO result = userProfileService.updateUserProfile(1L, testUserProfileDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testUserProfileDTO.getId(), result.getId());
        assertEquals(testUserProfileDTO.getBio(), result.getBio());
    }
}
