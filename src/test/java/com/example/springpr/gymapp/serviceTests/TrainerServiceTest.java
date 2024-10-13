package com.example.springpr.gymapp.serviceTests;

import com.example.springpr.gymapp.CountHelper;
import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private CountHelper countHelper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTrainerTest() {
        Trainer trainer = new Trainer(1L, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, "Yoga");

        trainerService.createTrainer(trainer);

        verify(trainerDAO, times(1)).createTrainer(trainer);
    }

    @Test
    void getTrainerByIdTest() {
        Long trainerId = 1L;
        Trainer expectedTrainer = new Trainer(trainerId, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, "Yoga");

        when(trainerDAO.getTrainerById(trainerId)).thenReturn(expectedTrainer);

        Trainer actualTrainer = trainerService.getTrainerById(trainerId);

        assertNotNull(actualTrainer);
        assertEquals(expectedTrainer.getId(), actualTrainer.getId());
        assertEquals(expectedTrainer.getFirstName(), actualTrainer.getFirstName());
        assertEquals(expectedTrainer.getLastName(), actualTrainer.getLastName());
        assertEquals(expectedTrainer.getUsername(), actualTrainer.getUsername());
        assertEquals(expectedTrainer.getPassword(), actualTrainer.getPassword());
        assertEquals(expectedTrainer.getIsActive(), actualTrainer.getIsActive());
        assertEquals(expectedTrainer.getTrainingType().getTrainingTypeName(), actualTrainer.getTrainingType().getTrainingTypeName());

        verify(trainerDAO, times(1)).getTrainerById(trainerId);
    }

    @Test
    void updateTrainerTest() {
        Trainer trainer = new Trainer(1L, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, "Yoga");


        long count = 2L;
        when(countHelper.countUser(trainer)).thenReturn(count);
        trainerService.updateTrainer(1L, trainer);

        verify(trainerDAO, times(1)).updateTrainer(1L, trainer, count);
    }

    @Test
    void deleteTrainerTest() {
        Long trainerId = 1L;

        trainerService.deleteTrainer(trainerId);

        verify(trainerDAO, times(1)).deleteTrainer(trainerId);
    }

    @Test
    void getAllTrainersTest() {
        List<Trainer> trainers = Arrays.asList(
                new Trainer(1L, "John", "Smith", "John.Smith", "password123",
                        true, "Personal Trainer"),
                new Trainer(2L, "Jane", "Doe", "Jane.Doe", "password456",
                        true, "Strength Coach"),
                new Trainer(3L, "Bob", "Brown", "Bob.Brown", "password789",
                        true,"Yoga Instructor")
        );

        when(trainerDAO.getAllTrainers()).thenReturn(trainers);

        List<Trainer> trainerList = trainerService.getAllTrainers();

        assertEquals(3, trainerList.size());
        verify(trainerDAO, times(1)).getAllTrainers();
    }
}
