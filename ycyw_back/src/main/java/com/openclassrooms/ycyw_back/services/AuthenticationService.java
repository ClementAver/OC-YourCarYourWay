package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.LoginRequest;
import com.openclassrooms.ycyw_back.dtos.LoginResponse;
import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationInterface {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager, JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * Authenticate a user
     * @param loginRequest The user's credentials
     * @return The authenticated user's refreshed tokens
     * @throws NotFoundException If the user is not found
     */
    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) throws NotFoundException {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .or(() -> userRepository.findByName(loginRequest.getName()))
                .orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        // Handle email or name authentication.
                        loginRequest.getEmail().isEmpty() ? loginRequest.getName() :  loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setRefreshToken(jwtRefreshToken);

        return loginResponse;
    }

    /**
     * Refresh a user's tokens
     * @param refreshToken The user's refresh token
     * @return The refreshed tokens
     * @throws NotFoundException If the user is not found
     */
    @Override
    public LoginResponse refresh(String refreshToken) throws NotFoundException {
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByName(username))
                .orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));

        if (jwtService.isTokenValid(refreshToken, user)) {
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateToken(user);
            return new LoginResponse(newAccessToken, newRefreshToken);
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}