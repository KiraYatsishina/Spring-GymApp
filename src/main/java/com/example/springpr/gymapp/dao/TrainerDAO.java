package com.example.springpr.gymapp.dao;

import com.example.springpr.gymapp.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainerDAO {
    private Map<Long, Trainer> trainers = new HashMap<Long, Trainer>();
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);


    public void createTrainer(Trainer trainer) {
        logger.info("Creating trainer : {}" + trainer);
        trainers.put(trainer.getId(), trainer);
    }

    public Trainer getTrainerById(long id) {
        logger.info("Getting trainer : {}", id);
        if(!trainers.containsKey(id)) {
            logger.warn("Get failed: Trainer with id {} not found", id);
            return null;
        }
        logger.info("Found trainer with id: {}", id);
        return trainers.get(id);
    }

    public void updateTrainer(Long id, Trainer trainer, long count) {
        logger.info("Updating trainer with id: {}", id);
        if(!trainers.containsKey(id)) {
            logger.warn("Update failed: Trainer with id {} not found", id);
            return;
        }
        Trainer curTrainer = trainers.get(id);

        if(trainer.getFirstName() != null) curTrainer.setFirstName(trainer.getFirstName());
        if(trainer.getLastName() != null) curTrainer.setLastName(trainer.getLastName());
        if(trainer.getPassword() != null) curTrainer.setPassword(trainer.getPassword());
        if(trainer.getIsActive() != curTrainer.getIsActive()) curTrainer.setIsActive(trainer.getIsActive());
        if(trainer.getTrainingType() != null) curTrainer.setTrainingType(trainer.getTrainingType());
        curTrainer.setCountAndPassword(count);

        logger.info("Updated trainer: {}", curTrainer);
    }

    public void deleteTrainer(Long id) {
        logger.info("Deleting trainer: {}", id);
        if(!trainers.containsKey(id)) {
            logger.warn("Delete failed: Trainer with id {} not found", id);
            return;
        }
        logger.info("Deleted trainer with id: {}", id);
        trainers.remove(id);
    }

    public List<Trainer> getAllTrainers() {
        return new ArrayList<>(trainers.values());
    }
}
