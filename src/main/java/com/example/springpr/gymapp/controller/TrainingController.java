package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.CreateTrainingDTO;
import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.mapper.TrainingMapper;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.service.TrainingService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Training Controller")
public class TrainingController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    private final TrainingService trainingService;

    @GetMapping("trainee/trainingList")
    @Operation(summary = "Get trainee training list", description = "Retrieve list of trainings for the authenticated trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    public ResponseEntity<?> getTraineeTrainingList(Principal principal,
                                                    @Parameter(description = "Start date to filter trainings")
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    @Parameter(description = "End date to filter trainings")
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                    @Parameter(description = "Trainer's name to filter trainings")
                                                    @RequestParam(required = false) String trainerName,
                                                    @Parameter(description = "Training type to filter trainings")
                                                    @RequestParam(required = false) String trainingType) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /trainee/trainingList, Request received for trainee: {}", transactionId, username);
        List<TrainingDTO> trainings = trainingService.findByTraineeUsername(username, fromDate, toDate, trainerName, trainingType);
        logger.info("Transaction ID: {}, Training list retrieved successfully for trainee: {}", transactionId, username);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @GetMapping("trainer/trainingList")
    @Operation(summary = "Get trainer training list", description = "Retrieve list of trainings for the authenticated trainer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    public ResponseEntity<?> getTrainerTrainingList(Principal principal,
                                                    @Parameter(description = "Start date to filter trainings")
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    @Parameter(description = "End date to filter trainings")
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                    @Parameter(description = "Trainee's name to filter trainings")
                                                    @RequestParam(required = false) String traineeName) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /trainer/trainingList, Request received for trainer: {}", transactionId, username);
        List<TrainingDTO> trainings = trainingService.findByTrainerUsername(username, fromDate, toDate, traineeName);
        logger.info("Transaction ID: {}, Training list retrieved successfully for trainer: {}", transactionId, username);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @PostMapping("trainer/addTraining")
    @Operation(summary = "Add training", description = "Allows a trainer to add a new training session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training added successfully",
                    content = @Content(schema = @Schema(implementation = TrainingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<?> addTraining(Principal principal, @RequestBody CreateTrainingDTO createTrainingDTO) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /trainer/addTraining, Request received to add training by trainer: {}", transactionId, username);

        try {
            Training training = trainingService.addTraining(createTrainingDTO);
            logger.info("Transaction ID: {}, Training added successfully for trainer: {}", transactionId, username);
            return ResponseEntity.status(HttpStatus.OK).body(TrainingMapper.toDTO(training, false));
        } catch (Exception e) {
            logger.error("Transaction ID: {}, Failed to add training for trainer: {}, Error: {}", transactionId, username, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}