package com.devshowcase.api.services;

import com.devshowcase.api.dtos.FeedbackRequestDTO;
import com.devshowcase.api.dtos.FeedbackResponseDTO;
import com.devshowcase.api.dtos.ProjectResponseDTO;
import com.devshowcase.api.exceptions.ResourceNotFoundException;
import com.devshowcase.api.models.Feedback;
import com.devshowcase.api.models.Project;
import com.devshowcase.api.repositories.FeedbackRepository;
import com.devshowcase.api.repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FeedbackRepository feedbackRepository;

    public ProjectService(ProjectRepository projectRepository, FeedbackRepository feedbackRepository) {
        this.projectRepository = projectRepository;
        this.feedbackRepository = feedbackRepository;
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