package com.trendy.cbs.controller;

import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.payload.request.UserStatusRequest;
import com.trendy.cbs.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user based on the provided request.
     *
     * @param userRequest the validated request body containing user details
     * @return {@link ResponseEntity} with {@link UserDTO} and HTTP 200 OK
     * @throws DuplicationResource if phone number already exists (handled as 409 Conflict)
     * @see UserService#createNewUser(UserRequest)
     */
    @PostMapping
    public ResponseEntity<@NonNull UserDTO> createUser(@Valid @RequestBody UserRequest userRequest){
        log.info("Creating user {}", userRequest.toString());
        return ResponseEntity.ok(userService.createNewUser(userRequest));
    }

    /**
     * Retrieves a list of all users.
     *
     * @return {@link ResponseEntity} containing a list of {@link UserDTO}s with HTTP 200 OK
     * @see UserService#getAllUsers()
     */
    @GetMapping
    public ResponseEntity<@NonNull List<UserDTO>> getAllUsers(){
        log.info("Retrieving all users.....");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique identifier of the user (must be positive Long)
     * @return {@link ResponseEntity} containing an {@link Optional} of {@link UserDTO}
     *         - 200 OK with UserDTO if user exists
     *         - 404 Not Found if user does not exist (via global exception handler)
     *
     * @throws IllegalArgumentException if id is null or negative
     * @see UserService#getUserById(Long)
     */
    @GetMapping({"/{id}"})
    public ResponseEntity<@NonNull Optional<UserDTO>> getUserById(@PathVariable Long id){
        log.info("Retrieving user {}", id.toString());
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Updates an existing user based on the provided ID and user request data.
     * <p>
     * This endpoint handles PUT requests to update user information. It validates the incoming request body
     * and delegates the update operation to the user service. Upon successful update, it returns the updated
     * user details in the response body with a 200 OK status.
     *
     * @param id The unique identifier of the user to be updated. This is extracted from the URL path.
     * @param userRequest The request body containing the updated user information. It must be a valid
     *                    {@link UserRequest} object, which is validated using {@link Valid}.
     * @return A {@link ResponseEntity} containing the updated {@link UserDTO} with a 200 OK status.
     *         The {@link NonNull} annotation ensures that the returned UserDTO is not null.
     */
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull UserDTO> updateUser(@PathVariable Long id,@Valid @RequestBody UserRequest userRequest){
        log.info("Updating user {}", userRequest.toString());
        return ResponseEntity.ok(userService.updateUser(id,userRequest));
    }

    /**
     * Updates the status of a user identified by the given ID.
     * *
     * This endpoint handles PATCH requests to update the status of a specific user.
     * It validates the request body and delegates the update operation to the user service.
     *
     * @param id The unique identifier of the user to update.
     * @param userStatusRequest The request object containing the new status for the user.
     * @return A ResponseEntity containing the updated UserDTO with an HTTP status of 200 (OK).
     */
    @PatchMapping("{id}")
    public ResponseEntity<@NotNull UserDTO> updateUserStatus(@PathVariable Long id,@Valid @RequestBody UserStatusRequest userStatusRequest){
        return ResponseEntity.ok(userService.updateUserStatus(id,userStatusRequest));
    }



}
