package com.example.springpr.gymapp;

import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.dao.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class CountHelper {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public CountHelper(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    public long countUser(String firstName, String lastName) {
        long countTrainee = traineeDAO.getAllTrainees().stream()
                .filter(existingTrainee ->
                        existingTrainee.getFirstName().equals(firstName) &&
                                existingTrainee.getLastName().equals(lastName))
                .count();

        long countTrainer = trainerDAO.getAllTrainers().stream()
                .filter(existingTrainer ->
                        existingTrainer.getFirstName().equals(firstName) &&
                                existingTrainer.getLastName().equals(lastName))
                .count();

        return countTrainee + countTrainer;
    }

}
