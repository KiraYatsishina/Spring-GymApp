package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.Trainer.TrainerDTO;
import com.example.springpr.gymapp.dto.Trainer.UpdateTrainerDTO;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trainer Controller", description = "Endpoints for trainer profile management")
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    private final TrainerService trainerService;

    @GetMapping("/myProfile")
    @Operation(summary = "Retrieve trainer profile", description = "Fetches the profile information for the authenticated trainer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TrainerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Trainer profile not found", content = @Content)
    })
    public ResponseEntity<TrainerDTO> getMyProfile(Principal principal) {
        String transactionId = UUID.randomUUID().toString();
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
    @Operation(summary = "Update trainer profile", description = "Updates the profile information of the authenticated trainer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<?> updateTrainerProfile(Principal principal, @RequestBody UpdateTrainerDTO updateTrainerDTO) {
        String transactionId = UUID.randomUUID().toString();
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
