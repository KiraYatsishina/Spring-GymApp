package com.example.springpr.gymapp.serviceTests;

import com.example.springpr.gymapp.dao.TrainingDAO;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTrainingTest() {
        Training training = new Training(1L, 1L, 1L, "Yoga Training", "Yoga", LocalDate.of(2024, 10, 1), 60);

        trainingService.createTraining(training);

        verify(trainingDAO, times(1)).createTraining(training);
    }

    @Test
    void getTrainingByIdTest() {
        Long trainingId = 1L;
        Training expectedTraining = new Training(trainingId, 1L, 1L, "Yoga Training", "Yoga", LocalDate.of(2024, 10, 1), 60);

        when(trainingDAO.getTrainingById(trainingId)).thenReturn(expectedTraining);

        Training actualTraining = trainingService.getTrainingById(trainingId);

        assertNotNull(actualTraining);
        assertEquals(expectedTraining.getId(), actualTraining.getId());
        assertEquals(expectedTraining.getTraineeId(), actualTraining.getTraineeId());
        assertEquals(expectedTraining.getTrainerId(), actualTraining.getTrainerId());
        assertEquals(expectedTraining.getTrainingName(), actualTraining.getTrainingName());
        assertEquals(expectedTraining.getTrainingType().getTrainingTypeName(), actualTraining.getTrainingType().getTrainingTypeName());
        assertEquals(expectedTraining.getTrainingDate(), actualTraining.getTrainingDate());
        assertEquals(expectedTraining.getTrainingDuration(), actualTraining.getTrainingDuration());

        verify(trainingDAO, times(1)).getTrainingById(trainingId);
    }

    @Test
    void getAllTrainingsTest() {
        List<Training> trainings = Arrays.asList(
                new Training(1L, 1L, 1L, "Yoga Training", "Yoga", LocalDate.of(2024, 10, 1), 60),
                new Training(2L, 2L, 1L, "Cardio Training", "Cardio", LocalDate.of(2024, 10, 2), 45)
        );

        when(trainingDAO.getAllTrainings()).thenReturn(trainings);

        List<Training> trainingList = trainingService.getAllTrainings();

        assertEquals(2, trainingList.size());
        verify(trainingDAO, times(1)).getAllTrainings();
    }
}
