package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.CreateTrainingDTO;
import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.mapper.TrainingMapper;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.service.TrainingService;
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
public class TrainingController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    private final TrainingService trainingService;

    @GetMapping("trainee/trainingList")
    public ResponseEntity<?> getTraineeTrainingList(Principal principal,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                    @RequestParam(required = false) String trainerName,
                                                    @RequestParam(required = false) String trainingType) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /trainee/trainingList", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /trainee/trainingList, Request received for trainee: {}", transactionId, username);
        List<TrainingDTO> trainings = trainingService.findByTraineeUsername(username, fromDate, toDate, trainerName, trainingType);
        logger.info("Transaction ID: {}, Training list retrieved successfully for trainee: {}", transactionId, username);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @GetMapping("trainer/trainingList")
    public ResponseEntity<?> getTrainerTrainingList(Principal principal,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                    @RequestParam(required = false) String traineeName) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /trainer/trainingList", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        logger.info("Transaction ID: {}, Endpoint: /trainer/trainingList, Request received for trainer: {}", transactionId, username);
        List<TrainingDTO> trainings = trainingService.findByTrainerUsername(username, fromDate, toDate, traineeName);
        logger.info("Transaction ID: {}, Training list retrieved successfully for trainer: {}", transactionId, username);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @PostMapping("trainer/addTraining")
    public ResponseEntity<?> addTraining(Principal principal, @RequestBody CreateTrainingDTO createTrainingDTO) {
        String transactionId = UUID.randomUUID().toString();
        if (principal == null) {
            logger.warn("Transaction ID: {}, Unauthorized access to /trainer/addTraining", transactionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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