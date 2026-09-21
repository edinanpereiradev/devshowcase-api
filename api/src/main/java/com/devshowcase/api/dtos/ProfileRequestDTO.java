package com.devshowcase.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfileRequestDTO(
        @NotBlank(message = "O nome nao pode ser vazio")
        String name,

        @NotBlank(message = "O email nao pode ser vazio")
        @Email(message = "Formato de email invalido")
        String email,

        String bio
) {}
