package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.*;
import com.example.springpr.gymapp.dto.Trainee.SignupTrainee;
import com.example.springpr.gymapp.dto.Trainer.SignupTrainer;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.AuthService;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Handles user authentication and registration")
public class AuthController{

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;


    @PostMapping("/auth")
    @Operation(summary = "Authenticate user", description = "Generates an authentication token for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<?> createAuthToken(@RequestBody UserDTO authRequest) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /auth, Request received: {}", transactionId, authRequest.getUsername());

        ResponseEntity<?> response = ResponseEntity.status(HttpStatus.OK).body(authService.createAuthToken(authRequest));

        return response;
    }

    @PostMapping("/signup/trainee")
    @Operation(summary = "Register a new trainee", description = "Registers a new trainee user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid trainee data", content = @Content)
    })
    ResponseEntity<?> signup(@RequestBody SignupTrainee traineeDTO) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /signup/trainee, Request received: {}", transactionId, traineeDTO);

        if (traineeDTO.getFirstName() == null || traineeDTO.getFirstName().length() < 1){
            logger.warn("Transaction ID: {}, Invalid trainee first name", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name length must be at least 1 character");
        }
        if (traineeDTO.getLastName() == null || traineeDTO.getLastName().length() < 1){
            logger.warn("Transaction ID: {}, Invalid trainee last name", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name length must be at least 1 character");
        }
        Trainee trainee = traineeService.mapToEntity(traineeDTO);
        Optional<UserDTO> createdUser = authService.signUpTrainee(trainee);
        if (createdUser.isPresent()) {
            logger.info("Transaction ID: {}, Trainee signup successful: {}", transactionId, traineeDTO.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }

        logger.error("Transaction ID: {}, Failed to sign up trainee: {}", transactionId, traineeDTO.getLastName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    @PostMapping("/signup/trainer")
    @Operation(summary = "Register a new trainer", description = "Registers a new trainer user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer registered successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid trainer data", content = @Content)
    })
    ResponseEntity<?> signup(@RequestBody SignupTrainer trainerDTO) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /signup/trainer, Request received: {}", transactionId, trainerDTO);

        if (trainerDTO.getFirstName() == null || trainerDTO.getFirstName().length() < 1){
            logger.warn("Transaction ID: {}, Invalid trainer first name", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name length must be at least 1 character");
        }
        if (trainerDTO.getLastName() == null || trainerDTO.getLastName().length() < 1){
            logger.warn("Transaction ID: {}, Invalid trainer last name", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name length must be at least 1 character");
        }
        Trainer trainer = trainerService.mapToEntity(trainerDTO);
        Optional<UserDTO> createdUser = authService.signUpTrainer(trainer);
        if(createdUser.isPresent()){
            logger.info("Transaction ID: {}, Trainer signup successful: {}", transactionId, trainerDTO.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }

        logger.error("Transaction ID: {}, Failed to sign up trainer: {}", transactionId, trainerDTO.getLastName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    @PutMapping("/changeLogin")
    @Operation(summary = "Change user's login", description = "Allows a user to change their login credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login changed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    public ResponseEntity<?> changeLogin(Principal principal, @RequestBody ChangeLoginRequest request) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /changeLogin, Request received for user: {}", transactionId, username);

        boolean changed = authService.changePassword(username, request.getOldPassword(), request.getNewPassword());
        if (changed) {
            logger.info("Transaction ID: {}, Password changed successfully for user: {}", transactionId, username);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        logger.warn("Transaction ID: {}, Failed to change password for user: {}", transactionId, username);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials or user not found");
    }

}
