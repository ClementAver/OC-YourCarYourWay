package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.LoginRequest;
import com.openclassrooms.ycyw_back.dtos.LoginResponse;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;

public interface AuthenticationInterface {
    LoginResponse authenticate(LoginRequest loginRequest) throws NotFoundException;

    LoginResponse refresh(String refreshToken) throws NotFoundException;
}

