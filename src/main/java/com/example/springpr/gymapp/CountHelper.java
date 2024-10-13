package com.example.springpr.gymapp;

import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class CountHelper {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public CountHelper(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    public long countUser(User user) {
        long countTrainee = traineeDAO.getAllTrainees().stream()
                .filter(existingTrainee ->
                        existingTrainee.getFirstName().equals(user.getFirstName()) &&
                                existingTrainee.getLastName().equals(user.getLastName()))
                .count();

        long countTrainer = trainerDAO.getAllTrainers().stream()
                .filter(existingTrainer ->
                        existingTrainer.getFirstName().equals(user.getFirstName()) &&
                                existingTrainer.getLastName().equals(user.getLastName()))
                .count();

        return countTrainee + countTrainer;
    }

}
