package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.Trainer.TrainerDTO;
import com.example.springpr.gymapp.dto.Trainer.UpdateTrainerDTO;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    private final TrainerService trainerService;

    @GetMapping("/myProfile")
    public ResponseEntity<TrainerDTO> getMyProfile(Principal principal) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /myProfile", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /myProfile, Request received for user: {}", transactionId, username);
        Optional<TrainerDTO> trainerDTOOptional = trainerService.findByUsername(username);

        if (trainerDTOOptional.isPresent()) {
            logger.info("Transaction ID: {}, Trainer profile found for user: {}", transactionId, username);
            return ResponseEntity.ok(trainerDTOOptional.get());
        } else {
            logger.warn("Transaction ID: {}, Trainer profile not found for user: {}", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateTrainerProfile(Principal principal, @RequestBody UpdateTrainerDTO updateTrainerDTO) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /updateProfile", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /updateProfile, Request received for user: {}", transactionId, username);
        if (updateTrainerDTO.getFirstName() == null || updateTrainerDTO.getFirstName().isEmpty()) {
            logger.warn("Transaction ID: {}, First name is missing", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        }
        if (updateTrainerDTO.getLastName() == null || updateTrainerDTO.getLastName().isEmpty()) {
            logger.warn("Transaction ID: {}, Last name is missing", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");
        }
        if (updateTrainerDTO.getSpecialization() == null || updateTrainerDTO.getSpecialization().isEmpty()) {
            logger.warn("Transaction ID: {}, Specialization is missing", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Specialization is required");
        }

        Optional<Trainer> updatedTrainer = trainerService.updateTrainerProfile(username, updateTrainerDTO);
        if (updatedTrainer.isPresent()) {
            TrainerDTO trainerDTO = TrainerMapper.toDTO(updatedTrainer.get(), true);
            logger.info("Transaction ID: {}, Trainer profile updated successfully for user: {}", transactionId, username);
            return ResponseEntity.ok(trainerDTO);
        }
        logger.error("Transaction ID: {}, Failed to update profile for user: {}", transactionId, username);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something with request");
    }

}
