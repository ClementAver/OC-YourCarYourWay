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
public class ChatResponse {
    int id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean pending;
    int customer;
    int employee;
}
