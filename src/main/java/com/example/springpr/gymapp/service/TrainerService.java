package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.CountHelper;
import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private CountHelper countHelper;

    public void createTrainer(Trainer trainer) {
        long count = countHelper.countUser(trainer.getFirstName(), trainer.getLastName());
        trainer.setCountAndPassword(count);
        trainerDAO.createTrainer(trainer);
    }

    public Trainer getTrainerById(Long id) {
        return trainerDAO.getTrainerById(id);
    }

    public void updateTrainer(Long id, Trainer trainer) {
        long count = countHelper.countUser(trainer.getFirstName(), trainer.getLastName());
        trainerDAO.updateTrainer(id, trainer, count);
    }

    public void deleteTrainer(Long id) {
        trainerDAO.deleteTrainer(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerDAO.getAllTrainers();
    }
}
