package com.devshowcase.api.services;

import com.devshowcase.api.dtos.FeedbackRequestDTO;
import com.devshowcase.api.dtos.FeedbackResponseDTO;
import com.devshowcase.api.dtos.ProjectRequestDTO;
import com.devshowcase.api.dtos.ProjectResponseDTO;
import com.devshowcase.api.exceptions.ResourceNotFoundException;
import com.devshowcase.api.models.Feedback;
import com.devshowcase.api.models.Profile;
import com.devshowcase.api.models.Project;
import com.devshowcase.api.models.Technology;
import com.devshowcase.api.repositories.FeedbackRepository;
import com.devshowcase.api.repositories.ProfileRepository;
import com.devshowcase.api.repositories.ProjectRepository;
import com.devshowcase.api.repositories.TechnologyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FeedbackRepository feedbackRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;

    public ProjectService(ProjectRepository projectRepository,
                          FeedbackRepository feedbackRepository,
                          ProfileRepository profileRepository,
                          TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.feedbackRepository = feedbackRepository;
        this.profileRepository = profileRepository;
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setRepositoryUrl(dto.repositoryUrl());

        // Busca e vincula o perfil do autor
        if (dto.profileId() != null) {
            Profile profile = profileRepository.findById(dto.profileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado para o ID: " + dto.profileId()));
            project.setProfile(profile);
        }

        // Vincula as tecnologias (se informadas no DTO)
        if (dto.technologyIds() != null && !dto.technologyIds().isEmpty()) {
            for (Long techId : dto.technologyIds()) {
                Technology tech = technologyRepository.findById(techId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tecnologia não encontrada para o ID: " + techId));
                project.getTechnologies().add(tech);
            }
        }

        project = projectRepository.save(project);
        return new ProjectResponseDTO(project);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> findAllPaged(String technology, Pageable pageable) {
        Page<Project> page = projectRepository.findByTechnologyName(technology, pageable);
        return page.map(ProjectResponseDTO::new);
    }

    @Transactional
    public ProjectResponseDTO incrementUpvote(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado para o ID: " + id));
        project.incrementUpvotes();
        project = projectRepository.save(project);
        return new ProjectResponseDTO(project);
    }

    @Transactional
    public FeedbackResponseDTO addFeedback(Long projectId, FeedbackRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado para o ID: " + projectId));

        Feedback feedback = new Feedback();
        feedback.setComment(dto.comment());
        feedback.setRating(dto.rating());
        feedback.setProject(project);

        feedback = feedbackRepository.save(feedback);

        // Atualiza a lista de feedbacks para recalcular a nota média
        project.getFeedbacks().add(feedback);
        project.updateAverageRating();
        projectRepository.save(project);

        return new FeedbackResponseDTO(feedback);
    }
}