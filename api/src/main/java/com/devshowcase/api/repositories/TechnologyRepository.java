package com.devshowcase.api.repositories;

import com.devshowcase.api.models.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {}