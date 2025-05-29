package com.openclassrooms.ycyw_back.controllers;

import com.openclassrooms.ycyw_back.dtos.LoginRequest;
import com.openclassrooms.ycyw_back.dtos.LoginResponse;
import com.openclassrooms.ycyw_back.dtos.UserRequest;
import com.openclassrooms.ycyw_back.dtos.UserResponse;
import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.exceptions.AlreadyExistException;
import com.openclassrooms.ycyw_back.exceptions.NoUserInContextException;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.services.AuthenticationService;
import com.openclassrooms.ycyw_back.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * Register then authenticate a new user.
     * @param userRequest The user to register
     * @return The registered then authenticated user's tokens
     * @throws AlreadyExistException If the user already exists
     * @throws NotFoundException If the user is not found
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("auth/register")
    public LoginResponse register(@Valid @RequestBody UserRequest userRequest) throws AlreadyExistException, NotFoundException {
        userService.createUser(userRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName(userRequest.getName());
        loginRequest.setEmail(userRequest.getEmail());
        loginRequest.setPassword(userRequest.getPassword());

        return authenticationService.authenticate(loginRequest);
    }

    /**
     * Authenticate a user
     * @param loginRequest The user's credentials
     * @return The authenticated user's refreshed tokens
     * @throws NotFoundException If the user is not found
     */
    @PostMapping("auth/login")
    public LoginResponse authenticate(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {
        return authenticationService.authenticate(loginRequest);
    }

    /**
     * Refresh the tokens of a user
     * @param refreshToken The refresh token
     * @return The authenticated user's refreshed tokens
     * @throws NotFoundException If the user is not found
     */
    @PostMapping("auth/refresh")
    public LoginResponse refresh(@RequestBody String refreshToken) throws NotFoundException {
        return authenticationService.refresh(refreshToken);
    }

    /**
     * Get the authenticated user
     * @return The authenticated user
     * @throws NoUserInContextException If no user is authenticated
     */
    @GetMapping("auth/me")
    public UserResponse authenticatedUser() throws NoUserInContextException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            User user = (User) authentication.getPrincipal();
            return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().toString(), user.getCreatedAt(), user.getUpdatedAt());
        } catch (Exception e) {
            throw new NoUserInContextException("Aucun utilisateur authentifié.");
        }
    }
}