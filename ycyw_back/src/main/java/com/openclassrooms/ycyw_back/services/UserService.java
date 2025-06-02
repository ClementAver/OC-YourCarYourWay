package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.UpdateUserRequest;
import com.openclassrooms.ycyw_back.dtos.UserRequest;
import com.openclassrooms.ycyw_back.dtos.UserResponse;
import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.enums.Role;
import com.openclassrooms.ycyw_back.exceptions.AlreadyExistException;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.mappers.UserResponseMapper;
import com.openclassrooms.ycyw_back.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
public class UserService implements UserInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseMapper userResponseMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserResponseMapper userResponseMapper ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userResponseMapper = userResponseMapper;
    }

    /**
     * Get a user by its identifier
     * @param id The user identifier
     * @return The user
     * @throws NotFoundException If the user is not found
     */
    @Override
    public UserResponse getUser(Integer id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));
        return userResponseMapper.apply(user);
    }


    /**
     * Get a user by its identifier
     * @param id The user identifier
     * @return The User instance
     * @throws NotFoundException If the user is not found
     */
    @Override
    public User getRawUser(Integer id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));
    }

    // Register
    /**
     * Create a new user
     * @param userRequest The user to create
     * @throws AlreadyExistException If the user already exists (email)
     */
    @Override
    public void createUser(UserRequest userRequest) throws AlreadyExistException {
        Optional<User> userInDB = userRepository.findByEmail(userRequest.getEmail());
        if (userInDB.isPresent()) {
            throw new AlreadyExistException("Cet email a déjà été renseigné.");
        }

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.save(user);

        // return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }

    /**
     * Update a user
     * @param id The user identifier
     * @param userRequest The updated user infos
     * @return The updated user
     * @throws NotFoundException If the user is not found
     */
    @Override
    public UserResponse updateUser(Integer id, UpdateUserRequest userRequest) throws NotFoundException {
        Optional<User> userInDB = userRepository.findById(id);
        if (userInDB.isPresent()) {
            User user = userInDB.get();
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            if (!Objects.equals(userRequest.getPassword(), "")) user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

            userRepository.save(user);
            return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().toString(), user.getCreatedAt(), user.getUpdatedAt());
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }
    }
}
