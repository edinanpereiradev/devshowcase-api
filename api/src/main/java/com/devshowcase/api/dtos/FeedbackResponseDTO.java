package com.devshowcase.api.dtos;

import com.devshowcase.api.models.Feedback;

public record FeedbackResponseDTO(Long id, String comment, Integer rating) {
    public FeedbackResponseDTO(Feedback entity) {
        this(entity.getId(), entity.getComment(), entity.getRating());
    }
}