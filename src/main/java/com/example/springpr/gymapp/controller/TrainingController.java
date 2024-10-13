package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private TrainingService trainingService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @Autowired
    public TrainingController(TrainingService trainingService) {
        logger.info("TrainingController instantiated");
        this.trainingService = trainingService;
    }

    @PostMapping
    public void createTraining(@RequestBody Training training) {
        logger.info("Creating training with ID: " + training.getId());
        trainingService.createTraining(training);
    }

    @GetMapping("/{id}")
    public Training getTrainingById(@PathVariable Long id) {
        logger.info("Retrieving training with ID: " + id);
        return trainingService.getTrainingById(id);
    }

    @GetMapping("/all")
    public List<Training> getAllTrainings() {
        logger.info("Getting all trainings");
        return trainingService.getAllTrainings();
    }
}