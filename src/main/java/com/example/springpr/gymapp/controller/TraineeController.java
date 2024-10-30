package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.Trainee.TraineeDTO;
import com.example.springpr.gymapp.dto.Trainee.UpdateTraineeDTO;
import com.example.springpr.gymapp.dto.Trainer.ShortTrainerDTO;
import com.example.springpr.gymapp.mapper.TraineeMapper;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import java.util.*;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
@Tag(name = "Trainee Controller", description = "Endpoints for trainee profile management")
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    private final TraineeService traineeService;

    @GetMapping("/myProfile")
    @Operation(summary = "Retrieve trainee profile", description = "Fetches the profile information for the authenticated trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TraineeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Trainee profile not found", content = @Content)
    })
    public ResponseEntity<TraineeDTO> getMyProfile(
            @Parameter(description = "Principal representing the authenticated trainee", required = true)
            Principal principal) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /myProfile, Request received for user: {}", transactionId, username);
        Optional<TraineeDTO> traineeDTOOptional = traineeService.findByUsername(username);

        if (traineeDTOOptional.isPresent()) {
            logger.info("Transaction ID: {}, Trainee profile found for user: {}", transactionId, username);
            return ResponseEntity.ok(traineeDTOOptional.get());
        } else {
            logger.warn("Transaction ID: {}, Trainee profile not found for user: {}", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/updateProfile")
    @Operation(summary = "Update trainee profile", description = "Updates the profile information of the authenticated trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = TraineeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<?> updateTraineeProfile(Principal principal, @RequestBody UpdateTraineeDTO updateTraineeDTO) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /updateProfile, Request received for user: {}", transactionId, username);

        if (updateTraineeDTO.getFirstName() == null || updateTraineeDTO.getFirstName().isEmpty()) {
            logger.warn("Transaction ID: {}, First name is missing", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        }
        if (updateTraineeDTO.getLastName() == null || updateTraineeDTO.getLastName().isEmpty()) {
            logger.warn("Transaction ID: {}, Last name is missing", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");
        }

        Optional<Trainee> updatedTrainee = traineeService.updateTraineeProfile(username, updateTraineeDTO);
        if (updatedTrainee.isPresent()) {
            TraineeDTO traineeDTO = TraineeMapper.toDTO(updatedTrainee.get(), true);
            logger.info("Transaction ID: {}, Profile updated successfully for user: {}", transactionId, username);
            return ResponseEntity.ok(traineeDTO);
        }

        logger.warn("Transaction ID: {}, Trainee not found for user: {}", transactionId, username);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainee not found");
    }


    @GetMapping("/notAssignedTrainersList")
    @Operation(summary = "Get not assigned trainers list", description = "Retrieves a list of trainers not assigned to the trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unassigned trainers list retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShortTrainerDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<List<ShortTrainerDTO>> getNotAssignedTrainersList(Principal principal) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /notAssignedTrainersList, Request received for Trainee: {}", transactionId, username);
        Optional<TraineeDTO> traineeDTOOptional = traineeService.findByUsername(username);
        if(!traineeDTOOptional.isPresent()) {
            logger.warn("Transaction ID: {}, Trainee not found for /notAssignedTrainersList request", transactionId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<ShortTrainerDTO> unassignedTrainers = traineeService.getNotAssignedTrainersList(username);
        logger.info("Transaction ID: {}, Unassigned trainers list retrieved for Trainee: {}", transactionId, username);
        return ResponseEntity.ok(unassignedTrainers);
    }

    @PutMapping("/updateTrainersList")
    @Operation(summary = "Update trainee's trainers list", description = "Updates the list of trainers assigned to the trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainers list updated successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShortTrainerDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid trainer usernames", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<?> updateTrainersList(Principal principal, @RequestBody List<String> trainerUsernames) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /updateTrainersList, Request received for user: {}", transactionId, username);
        List<ShortTrainerDTO> updatedTrainers = traineeService.updateTraineeTrainers(username, trainerUsernames);
        logger.info("Transaction ID: {}, Trainers list updated successfully for user: {}", transactionId, username);
        return ResponseEntity.ok(updatedTrainers);
    }
}
