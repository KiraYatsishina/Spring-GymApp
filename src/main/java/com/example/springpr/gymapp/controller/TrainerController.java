package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private TrainerService trainerService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @Autowired
    public TrainerController(TrainerService trainerService) {
        logger.info("TrainerController instantiated");
        this.trainerService = trainerService;
    }

    @PostMapping
    public void createTrainer(@RequestBody Trainer trainer) {
        logger.info("Creating trainer with ID: " + trainer.getId());
        trainerService.createTrainer(trainer);
    }

    @GetMapping("/{id}")
    public Trainer getTrainer(@PathVariable Long id) {
        logger.info("Retrieving trainer with ID: " + id);
        return trainerService.getTrainerById(id);
    }

    @GetMapping("/all")
    public List<Trainer> getAllTrainers() {
        logger.info("Getting all trainers");
        return trainerService.getAllTrainers();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrainer(@PathVariable Long id, @RequestBody Trainer trainer) {
        logger.info("Updating trainer with ID: " + id);
        trainerService.updateTrainer(id, trainer);
    }

    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable Long id) {
        logger.info("Deleting trainer with ID: " + id);
        trainerService.deleteTrainer(id);
    }
}
