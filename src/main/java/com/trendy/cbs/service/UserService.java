package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.payload.request.UserStatusRequest;
import com.trendy.cbs.payload.response.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createNewUser(@Valid UserRequest userRequest);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(Long id);

    UserDTO updateUser(Long id, @Valid UserRequest userRequest);

    UserDTO updateUserStatus(Long id, @Valid UserStatusRequest userStatusRequest);
}
