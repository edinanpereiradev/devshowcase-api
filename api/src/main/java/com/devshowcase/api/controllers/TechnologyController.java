package com.devshowcase.api.controllers;

import com.devshowcase.api.dtos.TechnologyRequestDTO;
import com.devshowcase.api.dtos.TechnologyResponseDTO;
import com.devshowcase.api.models.Technology;
import com.devshowcase.api.repositories.TechnologyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyRepository technologyRepository;

    public TechnologyController(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @PostMapping
    public ResponseEntity<TechnologyResponseDTO> create(@Valid @RequestBody TechnologyRequestDTO dto) {
        Technology tech = new Technology(dto.name());
        tech = technologyRepository.save(tech);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TechnologyResponseDTO(tech));
    }

    @GetMapping
    public ResponseEntity<List<TechnologyResponseDTO>> findAll() {
        List<TechnologyResponseDTO> list = technologyRepository.findAll().stream()
                .map(TechnologyResponseDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }
}
