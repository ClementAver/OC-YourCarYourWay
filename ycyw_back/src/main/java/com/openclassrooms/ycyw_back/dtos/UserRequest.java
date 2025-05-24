package com.openclassrooms.ycyw_back.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Le nom ne doit pas être vide.")
    @Size(max = 64, message = "Le nom ne doit pas dépasser 64 caractères.")
    String name; // Needed at register

    @NotNull(message = "Le courriel est obligatoire.")
    @Email(message = "L'adresse email doit être valide.")
    String email;

    @NotBlank(message = "Le mot de passe ne doit pas être vide.")
    @Size(max = 256, message = "Le mot ne passe ne doit pas dépasser 256 caractères.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Le mot de passe doit comporter au moins 8 caractères et contenir au moins un chiffre, une lettre minuscule, une majuscule et un caractère spécial.")
    String password;
}
