package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user based on the provided request.
     *
     * @param userRequest the user creation request (validated)
     * @return ResponseEntity containing the created UserDTO with HTTP 200 OK
     */
    @PostMapping
    public ResponseEntity<@NonNull UserDTO> createUser(@Valid @RequestBody UserRequest userRequest){
        log.info("Creating user {}", userRequest);
        return ResponseEntity.ok(userService.createNewUser(userRequest));
    }

    /**
     * Retrieves a list of all users.
     *
     * @return ResponseEntity containing a list of UserDTOs with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<@NonNull List<UserDTO>> getAllUsers(){
        log.info("Retrieving all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
