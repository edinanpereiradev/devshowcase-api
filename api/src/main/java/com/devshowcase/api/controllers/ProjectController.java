package com.devshowcase.api.controllers;

import com.devshowcase.api.dtos.ProjectRequestDTO;
import com.devshowcase.api.dtos.ProjectResponseDTO;
import com.devshowcase.api.models.Profile;
import com.devshowcase.api.models.Project;
import com.devshowcase.api.models.Technology;
import com.devshowcase.api.repositories.ProfileRepository;
import com.devshowcase.api.repositories.ProjectRepository;
import com.devshowcase.api.repositories.TechnologyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;

    public ProjectController(ProjectRepository projectRepository,
                             ProfileRepository profileRepository,
                             TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.technologyRepository = technologyRepository;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody ProjectRequestDTO dto) {
        Profile profile = profileRepository.findById(dto.profileId())
                .orElseThrow(() -> new RuntimeException("Perfil nao encontrado"));

        Project project = new Project();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setRepositoryUrl(dto.repositoryUrl());
        project.setProfile(profile);

        if (dto.technologyIds() != null && !dto.technologyIds().isEmpty()) {
            List<Technology> technologies = technologyRepository.findAllById(dto.technologyIds());
            project.setTechnologies(new HashSet<>(technologies));
        }

        project = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProjectResponseDTO(project));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> findAll() {
        List<ProjectResponseDTO> list = projectRepository.findAll().stream()
                .map(ProjectResponseDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }
}
