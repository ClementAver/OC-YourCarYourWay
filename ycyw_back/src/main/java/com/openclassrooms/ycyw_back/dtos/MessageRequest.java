package com.openclassrooms.ycyw_back.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @Size(max = 256, message = "Le contenu ne doit pas dépasser 256 caractères.")
    private String content;

    private int chat;
    private int user;
}
