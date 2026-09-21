package com.devshowcase.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import java.util.Set;

public record ProjectRequestDTO(
        @NotBlank(message = "O titulo é obrigatório")
        String title,

        String description,

        @URL(message = "Informe uma URL valida")
        String repositoryUrl,

        @NotNull(message = "O ID do perfil é obrigatório")
        Long profileId,

        Set<Long> technologyIds
) {}
