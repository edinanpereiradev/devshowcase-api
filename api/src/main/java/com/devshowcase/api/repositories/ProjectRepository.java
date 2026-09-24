package com.devshowcase.api.repositories;

import com.devshowcase.api.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.technologies t " +
            "WHERE (:techName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :techName, '%')))")
    Page<Project> findByTechnologyName(@Param("techName") String techName, Pageable pageable);
}