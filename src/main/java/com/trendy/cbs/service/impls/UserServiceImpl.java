package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.User;
import com.trendy.cbs.entity.UserProfile;
import com.trendy.cbs.enums.UserStatus;
import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.UserMapper;
import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.dto.UserWithProfile;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.payload.request.UserStatusRequest;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.repos.UserProfileRepository;
import com.trendy.cbs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
     * @return {@link UserDTO} of the saved user
     * @throws DuplicationResource if the phone number already exists in the system
     *
     * @implNote
     * - Checks for duplicate phone numbers via {@link UserProfileRepository#existsByPhoneNumber(Integer)}.
     * - Maps request to {@link User} and {@link UserProfile} using MapStruct.
     * - Sets bidirectional relationship between User and Profile.
     * - Persists both entities transactionally.
     */
    @Override
    public UserDTO createNewUser(UserRequest userRequest) {

        // Check phone number exists
        if (userProfileRepository.existsByPhoneNumber(Integer.valueOf(userRequest.getPhoneNumber()))) {
            throw new DuplicationResource("Phone number already exists");
        }

        // convert req to user
        User user = userMapper.toUser(userRequest);

        // assign active to status
        user.setStatus(UserStatus.PENDING_VERIFICATION);

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
     * @return list of {@link UserDTO}s
     *
     * @implNote
     * - Fetches all users via {@link UserRepository#findAll()}.
     * - Constructs {@link UserWithProfile} intermediates for mapping.
     * - Uses MapStruct's {@link UserMapper#toDtoList(List)} for efficient DTO conversion.
     */
    @Override
    public List<UserDTO> getAllUsers() {
        // fetch list of user entity
        List<User> users = userRepository.findAll();

        // use userList for mapping to UserWithProfile
        List<UserWithProfile> userWithProfile = users.stream()
                .map(user -> {
                    UserWithProfile uwp = new UserWithProfile();
                    uwp.setUser(user);
                    uwp.setProfile(user.getProfile());
                    return uwp;
                })
                .toList();

        // map to UserDTO List by mapstruct "toDtoList"
        return userMapper.toDtoList(userWithProfile);
    }

    /**
     * Fetches a user by ID, including their associated profile.
     *
     * @param id the unique identifier of the user
     * @return {@link Optional} containing {@link UserDTO} if user exists
     * @throws ResourceNotFoundException if no user with the given ID exists
     *
     * @implNote
     * - Validates user existence via repository.
     * - Eagerly loads the associated {@link UserProfile}.
     * - Maps to DTO using {@link UserMapper#toDto(User, UserProfile)}.
     */
    @Override
    public Optional<UserDTO> getUserById(Long id) {
        // validate ensure user exists
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User",id));

        // get user profile in user entity
        UserProfile profile = user.getProfile();

        // map to UserDTO
        UserDTO userDTO = userMapper.toDto(user, profile);

        return Optional.of(userDTO);
    }

    /**
     * Updates an existing user with the provided ID using the details from the user request.
     * <p>
     * This method first validates that the user exists by retrieving it from the repository. If the user
     * is not found, a {@link ResourceNotFoundException} is thrown. It then fetches or creates the user's profile,
     * copies the properties from the request to the existing profile, saves the updated profile, and
     * returns the updated user details as a {@link UserDTO}.
     *
     * @param id The unique identifier of the user to update.
     * @param userRequest The request object containing the updated user information.
     * @return The updated {@link UserDTO} representing the modified user.
     * @throws ResourceNotFoundException if no user is found with the given ID.
     */
    @Override
    public UserDTO updateUser(Long id, UserRequest userRequest) {

        // validate ensure user exists
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User",id));

        // Fetch existing profile (assuming User has a getProfile() method)
        UserProfile existingProfile = existUser.getProfile();

        if (existingProfile == null) {
            // Optionally create a new profile if none exists
            existingProfile = new UserProfile();
            existUser.setProfile(existingProfile);
        }

        // Copy properties from request to existing profile (ignores nulls if using ignoreProperties or custom logic)
        BeanUtils.copyProperties(userRequest, existingProfile);

        // Save the updated profile (cascades if configured in JPA)
        userProfileRepository.save(existingProfile);

        // Return DTO
        return userMapper.toDto(existUser, existingProfile);

    }

    /**
     * Updates the status of a user in the database.
     * *
     * This method retrieves the user by ID, updates their status based on the provided request,
     * saves the changes to the repository, and returns the updated user as a DTO.
     *
     * @param id The unique identifier of the user to update.
     * @param userStatusRequest The request object containing the new status.
     * @return The updated UserDTO representing the user after the status change.
     * @throws ResourceNotFoundException If no user is found with the given ID.
     * @Override
     */
    @Override
    public UserDTO updateUserStatus(Long id, UserStatusRequest userStatusRequest) {
        // validate ensure user exists
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User",id));

        existUser.setStatus(userStatusRequest.getStatus());

        System.out.println("User status: " + userStatusRequest.getStatus().toString());

        // save to db
        userRepository.save(existUser);

        return userMapper.toDto(existUser, existUser.getProfile());
    }


}
