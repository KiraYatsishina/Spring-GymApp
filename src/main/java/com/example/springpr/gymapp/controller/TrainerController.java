package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.TrainerDTO;
import com.example.springpr.gymapp.dto.UpdateTrainerDTO;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    @GetMapping("/myProfile")
    public ResponseEntity<TrainerDTO> getMyProfile(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<TrainerDTO> trainerDTOOptional = trainerService.findByUsername(username);

        return trainerDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateTrainerProfile(Principal principal, @RequestBody UpdateTrainerDTO updateTrainerDTO) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();

        if (updateTrainerDTO.getFirstName() == null || updateTrainerDTO.getFirstName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        if (updateTrainerDTO.getLastName() == null || updateTrainerDTO.getLastName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");
        if (updateTrainerDTO.getSpecialization() == null || updateTrainerDTO.getSpecialization().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Specialization is required");

        Optional<Trainer> updatedTrainer = trainerService.updateTrainerProfile(username, updateTrainerDTO);
        if (updatedTrainer.isPresent()) {
            TrainerDTO trainerDTO = TrainerMapper.toDTO(updatedTrainer.get(), true);
            return ResponseEntity.ok(trainerDTO);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something with request");
    }

    //@PutMapping("/updateTrainersList")

}
