package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.*;
import com.example.springpr.gymapp.dto.Trainee.SignupTrainee;
import com.example.springpr.gymapp.dto.Trainer.SignupTrainer;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.AuthService;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.TrainerService;
import com.example.springpr.gymapp.service.UserService;
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
public class AuthController{

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody UserDTO authRequest) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /auth, Request received: {}", transactionId, authRequest.getUsername());

        ResponseEntity<?> response = ResponseEntity.status(HttpStatus.OK).body(authService.createAuthToken(authRequest));

        return response;
    }

    @PostMapping("/signup/trainee") // регистрация
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

    @PostMapping("/signup/trainer") // регистрация
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

    @PutMapping("/trainee/changeLogin")
    ResponseEntity<?> changeLoginTrainee(Principal principal,
                                         @RequestBody ChangeLoginRequest request) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /trainee/changeLogin, Request received: {}", transactionId, principal.getName());

        return changeLogin(principal, request.getOldPassword(), request.getNewPassword(), transactionId);
    }

    @PutMapping("/trainer/changeLogin")
    ResponseEntity<?> changeLoginTrainer(Principal principal,
                                         @RequestBody ChangeLoginRequest request) {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /trainer/changeLogin, Request received: {}", transactionId, principal.getName());

        return changeLogin(principal, request.getOldPassword(), request.getNewPassword(), transactionId);
    }

    private ResponseEntity<?> changeLogin(Principal principal, String oldPassword, String newPassword, String transactionId) {
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized attempt to change login", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        logger.info("Transaction ID: {}, Changing login for user: {}", transactionId, username);
        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) {
            logger.error("Transaction ID: {}, User not found: {}", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        boolean changed = authService.changePassword(username, oldPassword, newPassword);
        if (changed) {
            logger.info("Transaction ID: {}, Password changed successfully for user: {}", transactionId, username);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        logger.warn("Transaction ID: {}, Failed to change password for user: {}", transactionId, username);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
