package com.devshowcase.api.controllers;

import com.devshowcase.api.dtos.ProfileRequestDTO;
import com.devshowcase.api.dtos.ProfileResponseDTO;
import com.devshowcase.api.models.Profile;
import com.devshowcase.api.repositories.ProfileRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @PostMapping
    public ResponseEntity<ProfileResponseDTO> create(@Valid @RequestBody ProfileRequestDTO dto) {
        Profile profile = new Profile(dto.name(), dto.email(), dto.bio());
        profile = profileRepository.save(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProfileResponseDTO(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> findById(@PathVariable Long id) {
        return profileRepository.findById(id)
                .map(profile -> ResponseEntity.ok(new ProfileResponseDTO(profile)))
                .orElse(ResponseEntity.notFound().build());
    }
}
