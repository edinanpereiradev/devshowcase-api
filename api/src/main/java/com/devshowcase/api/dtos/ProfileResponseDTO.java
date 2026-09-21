package com.devshowcase.api.dtos;

import com.devshowcase.api.models.Profile;

public record ProfileResponseDTO(Long id, String name, String email, String bio) {
    public ProfileResponseDTO(Profile entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getBio());
    }
}
