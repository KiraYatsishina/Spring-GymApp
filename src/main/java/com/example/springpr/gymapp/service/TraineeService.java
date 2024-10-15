package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.CountHelper;
import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private CountHelper countHelper;

    public void createTrainee(Trainee trainee) {
        long count = countHelper.countUser(trainee.getFirstName(), trainee.getLastName());
        trainee.setCountAndPassword(count);
        traineeDAO.createTrainee(trainee);
    }

    public Trainee getTraineeById(Long id) {
        return traineeDAO.getTraineeById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeDAO.getAllTrainees();
    }

    public void updateTrainee(Long id, Trainee trainee) {
        long count = countHelper.countUser(trainee.getFirstName(), trainee.getLastName());
        traineeDAO.updateTrainee(id, trainee, count);
    }

    public void deleteTrainee(Long id) {
        traineeDAO.deleteTrainee(id);
    }
}
