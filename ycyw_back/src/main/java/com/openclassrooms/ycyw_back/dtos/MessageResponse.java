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
public class MessageResponse {
    int id;
    String content;
    LocalDateTime createdAt;
    int user;
    int chat;
}
