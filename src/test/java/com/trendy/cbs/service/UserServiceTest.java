package com.trendy.cbs.service;

import com.trendy.cbs.entity.User;
import com.trendy.cbs.entity.UserProfile;
import com.trendy.cbs.enums.UserStatus;
import com.trendy.cbs.mapper.UserMapper;
import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.dto.UserProfileDTO;
import com.trendy.cbs.payload.dto.UserWithProfile;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserProfileRepository userProfileRepository;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private UserProfile profile;
    private UserDTO userDTO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        userRequest = UserRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("1234567890")
                .build(); // Assume UserRequest has these fields based on UserProfile

        profile = new UserProfile();
        profile.setFirstName("Jane");
        profile.setLastName("Doe");
        profile.setEmail("jane.doe@example.com");
        profile.setPhoneNumber(1234567890);

        user = User.builder()
                .status(UserStatus.PENDING_VERIFICATION)
                .createdAt(now)
                .updatedAt(now)
                .profile(profile)
                .build();

        UserProfileDTO profileDTO = UserProfileDTO.builder() // Assume UserProfileDTO mirrors UserProfile
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber(1234567890)
                .build();

        userDTO = UserDTO.builder()
                .userId(456L)
                .profile(profileDTO)
                .status(UserStatus.PENDING_VERIFICATION)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
    @Test
    @DisplayName("Should create and return UserDTO when no duplicate phone")
    void createNewUser_NoDuplicate_CreatesUser() {
        // Given
        when(userProfileRepository.existsByPhoneNumber(1234567890)).thenReturn(false);
        when(userMapper.toUser(userRequest)).thenReturn(user);
        when(userMapper.toProfile(userRequest)).thenReturn(profile);
        when(userRepository.save(user)).thenAnswer(invocation -> {
            user.setUserId(456L); // Simulate ID generation
            return user;
        });
        when(userProfileRepository.save(profile)).thenReturn(profile);
        when(userMapper.toDto(user, profile)).thenReturn(userDTO);

        // When
        UserDTO result = userService.createNewUser(userRequest);

        // Then
        assertEquals(userDTO, result);
        assertEquals(UserStatus.PENDING_VERIFICATION, user.getStatus());
        verify(userRepository).save(user);
        verify(userProfileRepository).save(profile);
    }
    @Test
    @DisplayName("Should return list of UserDTOs when users exist")
    void getAllUsers_UsersExist_ReturnsList() {
        // Given
        List<User> users = List.of(user);
        UserWithProfile uwp = new UserWithProfile();
        uwp.setUser(user);
        uwp.setProfile(profile);
        List<UserWithProfile> uwps = List.of(uwp);
        List<UserDTO> dtos = List.of(userDTO);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDtoList(uwps)).thenReturn(dtos);

        // When
        List<UserDTO> result = userService.getAllUsers();

        // Then
        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no users")
    void getAllUsers_NoUsers_ReturnsEmpty() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        List<UserDTO> result = userService.getAllUsers();

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }
}
