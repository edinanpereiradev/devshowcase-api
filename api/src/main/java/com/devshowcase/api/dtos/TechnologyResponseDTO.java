package com.devshowcase.api.dtos;

import com.devshowcase.api.models.Technology;

public record TechnologyResponseDTO(Long id, String name) {
    public TechnologyResponseDTO(Technology entity) {
        this(entity.getId(), entity.getName());
    }
}
