package com.openclassrooms.ycyw_back.mappers;

import com.openclassrooms.ycyw_back.dtos.UserResponse;
import com.openclassrooms.ycyw_back.entities.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserResponseMapper implements Function<User, UserResponse> {
    @Override
    public UserResponse apply(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().toString(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
