package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.payload.response.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface UserService {
    UserDTO createNewUser(@Valid UserRequest userRequest);

    List<UserDTO> getAllUsers();
}
