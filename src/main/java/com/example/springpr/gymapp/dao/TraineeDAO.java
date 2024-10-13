package com.example.springpr.gymapp.dao;

import com.example.springpr.gymapp.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraineeDAO {

    private Map<Long, Trainee> trainees = new HashMap<Long, Trainee>();
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);


    public void createTrainee(Trainee trainee) {
        logger.info("Creating trainee: {}", trainee);
        trainees.put(trainee.getId(), trainee);
    }

    public Trainee getTraineeById(long id) {
        logger.info("Getting trainee by id: {}", id);
        if (!trainees.containsKey(id)) {
            logger.warn("Get failed: Trainee with id {} not found", id);
            return null;
        }
        logger.info("Found trainee with id: {}", id);
        return trainees.get(id);
    }

    public void updateTrainee(Long id, Trainee trainee, long count) {
        logger.info("Updating trainee with id {}", id);
        if(!trainees.containsKey(id)) {
            logger.warn("Update failed: Trainee with id: {} not found", id);
            return;
        }
        Trainee curTrainee = trainees.get(id);

        if(trainee.getFirstName() != null) curTrainee.setFirstName(trainee.getFirstName());
        if(trainee.getLastName() != null) curTrainee.setLastName(trainee.getLastName());
        if(trainee.getPassword() != null) curTrainee.setPassword(trainee.getPassword());
        if(trainee.getIsActive() != curTrainee.getIsActive()) curTrainee.setIsActive(trainee.getIsActive());
        if(trainee.getDateOfBirth() != null) curTrainee.setDateOfBirth(trainee.getDateOfBirth());
        if(trainee.getAddress() != null) curTrainee.setAddress(trainee.getAddress());
        curTrainee.setUsernameCountPassword(count);
        logger.info("Updated trainee: {}", curTrainee);
    }

    public void deleteTrainee(Long id) {
        logger.info("Deleting trainee: {}", id);
        if(!trainees.containsKey(id)) {
            logger.warn("Delete failed: Trainee with id {} not found", id);
            return;
        }
        logger.info("Deleted trainee with id: {}", id);
        trainees.remove(id);
    }

    public List<Trainee> getAllTrainees() {
        return new ArrayList<>(trainees.values());
    }

}
