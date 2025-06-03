package com.openclassrooms.ycyw_back.dtos;

import jakarta.validation.constraints.NotNull;
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
    String content;

    @NotNull(message = "Le chat est obligatoire.")
    int chat;

    @NotNull(message = "L'utilisateur est obligatoire.")
    int user;
}
