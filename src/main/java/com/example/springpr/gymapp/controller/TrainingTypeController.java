package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.TrainingTypeDTO;
import com.example.springpr.gymapp.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping("/allTrainingTypes")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        List<TrainingTypeDTO> trainingTypes = trainingTypeService.getAllTrainingTypes();
        return ResponseEntity.ok(trainingTypes); // Return the list of training types
    }

}
