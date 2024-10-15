package com.example.springpr.gymapp.daoTests;

import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.dao.TrainingDAO;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDAOTest {

    @InjectMocks
    private TrainingDAO trainingDAO;

    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        traineeDAO = new TraineeDAO();
        traineeDAO.createTrainee(new Trainee(1L, "John", "Doe", 0L, true, null, null));
        traineeDAO.createTrainee(new Trainee(2L, "John", "Doe", 1L, true, null, null));
        traineeDAO.createTrainee(new Trainee(3L, "Alex", "Smith", 0L, true, null, null));

        trainerDAO = new TrainerDAO();
        trainerDAO.createTrainer(new Trainer(1L, "Alex", "Smith", 1L, true, "Yoga"));
        trainerDAO.createTrainer(new Trainer(2L, "Sam", "White", 0L, true, "Pilates"));
        trainerDAO.createTrainer(new Trainer(3L, "Sam", "White", 1L, true, "Strength"));

        trainingDAO = new TrainingDAO(traineeDAO, trainerDAO);
    }

    @Test
    void createTrainingSuccessTest() {
        Long trainingId = 1L;
        Training expectedTraining = new Training(trainingId, 1L, 1L,
                "Yoga Training", "Yoga",
                LocalDate.of(2024, 10, 1), 60);


        trainingDAO.createTraining(expectedTraining);
        Training retrievedTraining = trainingDAO.getTrainingById(trainingId);

        assertNotNull(retrievedTraining);
        assertEquals(expectedTraining, retrievedTraining);
    }

    @Test
    void createTraining_TraineeNotFoundTest() {
        Long trainingId = 1L;
        Training expectedTraining = new Training(trainingId, 10L, 1L,
                "Yoga Training", "Yoga",
                LocalDate.of(2024, 10, 1), 60);


        trainingDAO.createTraining(expectedTraining);
        Training retrievedTraining = trainingDAO.getTrainingById(trainingId);

        assertNull(retrievedTraining);
    }


    @Test
    void createTraining_TrainerNotFoundTest() {
        Long trainingId = 1L;
        Training expectedTraining = new Training(trainingId, 1L, 10L,
                "Yoga Training", "Yoga",
                LocalDate.of(2024, 10, 1), 60);


        trainingDAO.createTraining(expectedTraining);
        Training retrievedTraining = trainingDAO.getTrainingById(trainingId);

        assertNull(retrievedTraining);
    }

    @Test
    void getTrainingByIdTest() {
        Long trainingId = 1L;
        Training expectedTraining = new Training(trainingId, 1L, 1L,
                "Yoga Training", "Yoga",
                LocalDate.of(2024, 10, 1), 60);

        trainingDAO.createTraining(expectedTraining);
        Training retrievedTraining = trainingDAO.getTrainingById(trainingId);

        assertNotNull(retrievedTraining);
        assertEquals(expectedTraining, retrievedTraining);
    }

    @Test
    void getNonExistentTrainingByIdTest() {
        Training retrievedTraining = trainingDAO.getTrainingById(10L);
        assertNull(retrievedTraining);
    }

    @Test
    void getAllTrainingsTest() {
        Training training1 = new Training(1L, 1L, 1L,
                "Yoga Training", "Yoga",
                LocalDate.of(2024, 10, 1), 60);
        Training training2 = new Training(2L, 3L, 3L,
                "Yoga Training", "Yoga",
                LocalDate.of(2023, 1, 1), 45);

        trainingDAO.createTraining(training1);
        trainingDAO.createTraining(training2);

        List<Training> trainings = trainingDAO.getAllTrainings();
        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(training1));
        assertTrue(trainings.contains(training2));
    }
}
