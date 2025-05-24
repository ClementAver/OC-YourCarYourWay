package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.UpdateUserRequest;
import com.openclassrooms.ycyw_back.dtos.UserRequest;
import com.openclassrooms.ycyw_back.dtos.UserResponse;
import com.openclassrooms.ycyw_back.exceptions.AlreadyExistException;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;

public interface UserInterface {

    // Register
    void createUser(UserRequest userRequest) throws AlreadyExistException;

    UserResponse getUser(Integer id) throws NotFoundException;

    UserResponse updateUser(Integer id, UpdateUserRequest userRequest) throws NotFoundException;
}

