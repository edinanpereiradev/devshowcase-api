package com.devshowcase.api.repositories;

import com.devshowcase.api.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {}