package com.example.springpr.gymapp.dao;

import com.example.springpr.gymapp.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingDAO {
    private Map<Long, Training> trainingStorage = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);


    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public TrainingDAO(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    public void createTraining(Training training) {
        logger.info("Creating training: {}" + training);
        if (traineeDAO.getTraineeById(training.getTraineeId()) == null) {
            logger.warn("Creation training failed: Trainee with id {} not found", training.getTraineeId());
            return;
        }
        if (trainerDAO.getTrainerById(training.getTrainerId()) == null) {
            logger.warn("Creation training failed: Trainer with id {} not found", training.getTrainerId());
            return;
        }
        logger.info("Creating training successful");
        trainingStorage.put(training.getId(), training);
    }

    public Training getTrainingById(Long id) {
        logger.info("Getting training with id: {}", id);
        if(!trainingStorage.containsKey(id)) {
            logger.warn("Get failed: Training with id {} not found", id);
            return null;
        }
        logger.info("Found training with id {} found", id);
        return trainingStorage.get(id);
    }

    public List<Training> getAllTrainings() {
        return new ArrayList<>(trainingStorage.values());
    }
}
