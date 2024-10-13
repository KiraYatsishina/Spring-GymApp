package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dao.TrainingDAO;
import com.example.springpr.gymapp.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;

    public void createTraining(Training training) {
        trainingDAO.createTraining(training);
    }

    public Training getTrainingById(Long id) {
        return trainingDAO.getTrainingById(id);
    }

    public List<Training> getAllTrainings() {
        return trainingDAO.getAllTrainings();
    }
}
