package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.dto.Trainee.TraineeDTO;
import com.example.springpr.gymapp.dto.Trainee.UpdateTraineeDTO;
import com.example.springpr.gymapp.dto.Trainer.ShortTrainerDTO;
import com.example.springpr.gymapp.mapper.TraineeMapper;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    private final UserService userService;
    private final TraineeService traineeService;
    private final JwtCore jwtCore;

    @GetMapping("/myProfile")
    public ResponseEntity<TraineeDTO> getMyProfile(Principal principal) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /myProfile", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

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
    public ResponseEntity<?> updateTraineeProfile(Principal principal, @RequestBody UpdateTraineeDTO updateTraineeDTO) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /updateProfile", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
            UserDetails userDetails = userService.loadUserByUsername(updatedTrainee.get().getUsername());
            String newToken = jwtCore.generateToken(userDetails);
            TraineeDTO traineeDTO = TraineeMapper.toDTO(updatedTrainee.get(), true);
            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("trainee", traineeDTO);

            logger.info("Transaction ID: {}, Profile updated successfully for user: {}", transactionId, username);
            return ResponseEntity.ok(response);
        }

        logger.error("Transaction ID: {}, Failed to update profile for user: {}", transactionId, username);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something with request");
    }


    @GetMapping("/notAssignedTrainersList")
    public ResponseEntity<List<ShortTrainerDTO>> getNotAssignedTrainersList(Principal principal) {
        String transactionId = UUID.randomUUID().toString();
        if(principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /notAssignedTrainersList", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
    public ResponseEntity<?> updateTrainersList(Principal principal, @RequestBody List<String> trainerUsernames) {
        String transactionId = UUID.randomUUID().toString();
        if(principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /updateTrainersList", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /updateTrainersList, Request received for user: {}", transactionId, username);
        List<ShortTrainerDTO> updatedTrainers = traineeService.updateTraineeTrainers(username, trainerUsernames);
        logger.info("Transaction ID: {}, Trainers list updated successfully for user: {}", transactionId, username);
        return ResponseEntity.ok(updatedTrainers);
    }
}
