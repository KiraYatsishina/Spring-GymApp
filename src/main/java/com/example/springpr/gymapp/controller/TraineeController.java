package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/trainees")
public class TraineeController {

    private TraineeService traineeService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @Autowired
    public TraineeController(TraineeService traineeService) {
        logger.info("TraineeController instantiated");
        this.traineeService = traineeService;
    }

    @PostMapping
    public void createTrainee(@RequestBody Trainee trainee) {
        logger.info("Creating trainee with ID: " + trainee.getId());
        traineeService.createTrainee(trainee);
    }

    @GetMapping("/{id}")
    public Trainee  getTraineeById(@PathVariable Long id) {
        logger.info("Retrieving trainee with ID: " + id);
         return traineeService.getTraineeById(id);
    }

    @GetMapping("/all")
    public List<Trainee> getAllTrainees() {
        logger.info("Getting all trainees");
        return traineeService.getAllTrainees();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Явно при успешном обновлении будет возвращен статус 204
    public void updateTrainee(@PathVariable Long id, @RequestBody Trainee trainee) {
        logger.info("Updating trainee with ID: " + id);
        traineeService.updateTrainee(id, trainee);
    }

    @DeleteMapping("/{id}")
    public void deleteTrainee(@PathVariable Long id) {
        logger.info("Deleting trainee with ID: " + id);
        traineeService.deleteTrainee(id);
    }
}
