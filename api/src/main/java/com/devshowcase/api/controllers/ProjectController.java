package com.devshowcase.api.controllers;

import com.devshowcase.api.dtos.FeedbackRequestDTO;
import com.devshowcase.api.dtos.FeedbackResponseDTO;
import com.devshowcase.api.dtos.ProjectRequestDTO;
import com.devshowcase.api.dtos.ProjectResponseDTO;
import com.devshowcase.api.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET /api/projects?technology=java&page=0&size=10
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(
            @RequestParam(required = false) String technology,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<ProjectResponseDTO> list = projectService.findAllPaged(technology, pageable);
        return ResponseEntity.ok(list);
    }

    // PUT /api/projects/{id}/upvote
    @PutMapping("/{id}/upvote")
    public ResponseEntity<ProjectResponseDTO> upvote(@PathVariable Long id) {
        ProjectResponseDTO dto = projectService.incrementUpvote(id);
        return ResponseEntity.ok(dto);
    }

    // POST /api/projects/{id}/feedbacks
    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<FeedbackResponseDTO> addFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO dto) {
        FeedbackResponseDTO feedbackDto = projectService.addFeedback(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackDto);
    }
}