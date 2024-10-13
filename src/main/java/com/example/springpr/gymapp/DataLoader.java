package com.example.springpr.gymapp;

import com.example.springpr.gymapp.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.dao.TrainingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DataLoader {

    @Autowired
    private TraineeDAO traineeDAO;
    @Autowired
    private TrainerDAO trainerDAO;
    @Autowired
    private TrainingDAO trainingDAO;

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Value("${data.trainees.file}")
    private String traineeDataPath;

    @Value("${data.trainers.file}")
    private String trainerDataPath;

    @Value("${data.training.file}")
    private String trainingDataPath;


    @PostConstruct
    public void loadTrainees() throws IOException {
        Resource resource = new ClassPathResource(traineeDataPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainee trainee = new Trainee();
                trainee.setId(Long.valueOf(data[0]));
                trainee.setFirstName(data[1]);
                trainee.setLastName(data[2]);
                trainee.setUsername(data[3]);
                trainee.setPassword(data[4]);
                trainee.setIsActive(Boolean.parseBoolean(data[5]));

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate birthDate = LocalDate.parse(data[6], dateFormatter);
                    trainee.setDateOfBirth(birthDate);
                } catch (DateTimeParseException e) {
                    trainee.setDateOfBirth(null);
                    logger.error("Invalid date format for trainee ID {}: {}", data[0], e.getMessage());
                }
                catch (Exception e) {
                    logger.error("Failed to create trainee from line: {}", line, e);
                }
                trainee.setAddress(data[7]);
                traineeDAO.createTrainee(trainee);
            }
        }
    }

    @PostConstruct
    public void loadTrainers() throws IOException {
        Resource resource = new ClassPathResource(trainerDataPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                try {
                    Trainer trainer = new Trainer();
                    trainer.setId(Long.valueOf(data[0]));
                    trainer.setFirstName(data[1]);
                    trainer.setLastName(data[2]);
                    trainer.setUsername(data[3]);
                    trainer.setPassword(data[4]);
                    trainer.setIsActive(Boolean.parseBoolean(data[5]));
                    trainer.setTrainingType(new TrainingType(data[6]));
                    trainerDAO.createTrainer(trainer);
                } catch (Exception e) {
                    logger.error("Failed to create trainer from line: {}", line, e);
                }
            }
        }
    }

    @PostConstruct
    public void loadTrainings() throws IOException {
        Resource resource = new ClassPathResource(trainingDataPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                try {
                    Training training = new Training();
                    training.setId(Long.valueOf(data[0]));
                    training.setTraineeId(Long.valueOf(data[1]));
                    training.setTrainerId(Long.valueOf(data[2]));
                    training.setTrainingName(data[3]);
                    training.setTrainingType(new TrainingType(data[4]));

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate trainingDate = LocalDate.parse(data[5], dateFormatter);
                    training.setTrainingDate(trainingDate);

                    training.setTrainingDuration(Integer.parseInt(data[6]));
                    trainingDAO.createTraining(training);
                } catch (DateTimeParseException e) {
                    logger.error("Invalid date format for training ID {}: {}", data[0], e.getMessage());
                } catch (Exception e) {
                    logger.error("Failed to create training from line: {}", line, e);
                }
            }
        }
    }
}