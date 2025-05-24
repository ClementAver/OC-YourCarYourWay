package com.openclassrooms.ycyw_back.controllers;

import com.openclassrooms.ycyw_back.dtos.*;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get a user by its identifier
     * @param id The user identifier
     * @return The user
     * @throws NotFoundException If the user is not found
     */
    @GetMapping("/user/{id}")
    public UserResponse getUser(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return userService.getUser(id);
    }

    /**
     * Update a user
     * @param id The user identifier
     * @param userRequest The user to update
     * @return The updated user
     * @throws NotFoundException If the user is not found
     */
    @PutMapping("/user/{id}")
    public UserResponse updateUser(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id, @Valid @RequestBody UpdateUserRequest userRequest) throws NotFoundException {
            return userService.updateUser(id, userRequest);
    }
}