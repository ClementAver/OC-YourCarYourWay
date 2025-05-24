package com.openclassrooms.ycyw_back.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    int id;
    String name;
    String email;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
