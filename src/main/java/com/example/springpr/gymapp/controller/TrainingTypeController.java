package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.TrainingTypeDTO;
import com.example.springpr.gymapp.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TrainingTypeController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeController.class);

    private final TrainingTypeService trainingTypeService;

    @GetMapping("/allTrainingTypes")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        String transactionId = UUID.randomUUID().toString();
        logger.info("Transaction ID: {}, Endpoint: /allTrainingTypes, Request to retrieve all training types", transactionId);

        List<TrainingTypeDTO> trainingTypes = trainingTypeService.getAllTrainingTypes();
        logger.info("Transaction ID: {}, Successfully retrieved {} training types", transactionId, trainingTypes.size());
        return ResponseEntity.ok(trainingTypes);
    }

}
