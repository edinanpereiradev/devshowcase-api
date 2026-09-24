package com.devshowcase.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = true) // Alterado para profile_id
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false) // Declarado o relacionamento com Project
    private Project project;

    public Feedback() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    import com.fasterxml.jackson.annotation.JsonIgnore; // Import necessário

    @Entity
    public class Feedback {

        // ... outros atributos ...

        @ManyToOne
        @JoinColumn(name = "project_id")
        @JsonIgnore // <-- ADICIONE ESTA LINHA
        private Project project;

        // ... getters e setters ...
    }
