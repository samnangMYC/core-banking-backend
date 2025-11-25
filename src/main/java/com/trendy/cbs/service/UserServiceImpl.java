package com.trendy.cbs.service;

import com.trendy.cbs.entity.User;
import com.trendy.cbs.entity.UserProfile;
import com.trendy.cbs.enums.UserStatus;
import com.trendy.cbs.mapper.UserMapper;
import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.dto.UserWithProfile;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user from the request, sets active status, initializes profile,
     * saves entities, and returns the DTO.
     *
     * @param userRequest the user creation request
     * @return UserDTO of the saved user
     */
    @Override
    public UserDTO createNewUser(UserRequest userRequest) {
        // convert req to user
        User user = userMapper.toUser(userRequest);
        // assign active to status
        user.setStatus(UserStatus.ACTIVE);

        // initialize UserProfile by filter from req using mapstruct "toProfile"
        UserProfile profile = userMapper.toProfile(userRequest);
        user.setProfile(profile);
        profile.setUser(user);

        // save to userEntity
        User savedUser = userRepository.save(user);
        userProfileRepository.save(profile);

        return userMapper.toDto(savedUser,profile);

    }

    /**
     * Retrieves all users with their profiles and maps to a list of DTOs.
     *
     * @return list of UserDTOs
     */
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserWithProfile> combined = users.stream()
                .map(user -> {
                    UserWithProfile uwp = new UserWithProfile();
                    uwp.setUser(user);
                    uwp.setProfile(user.getProfile());
                    return uwp;
                })
                .toList();

        return userMapper.toDtoList(combined);
    }
}
