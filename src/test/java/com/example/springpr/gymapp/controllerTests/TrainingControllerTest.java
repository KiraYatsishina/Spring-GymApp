package com.example.springpr.gymapp.controllerTests;

import com.example.springpr.gymapp.controller.TrainingController;
import com.example.springpr.gymapp.dto.CreateTrainingDTO;
import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.model.*;
import com.example.springpr.gymapp.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");
    }

//    @Test
//    void testGetTraineeTrainingList_Success() {
//        List<TrainingDTO> mockTrainings = Collections.singletonList(new TrainingDTO());
//        LocalDate fromDate = LocalDate.now().minusDays(7);
//        LocalDate toDate = LocalDate.now();
//
//        when(trainingService.findByTraineeUsername("testUser", fromDate, toDate, null, null)).thenReturn(mockTrainings);
//
//        ResponseEntity<?> response = trainingController.getTraineeTrainingList(mockPrincipal, fromDate, toDate, null, null);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockTrainings, response.getBody());
//        verify(trainingService, times(1)).findByTraineeUsername("testUser", fromDate, toDate, null, null);
//    }
//
//    @Test
//    void testGetTrainerTrainingList_Success() {
//        List<TrainingDTO> mockTrainings = Collections.singletonList(new TrainingDTO());
//        LocalDate fromDate = LocalDate.now().minusDays(7);
//        LocalDate toDate = LocalDate.now();
//
//        when(trainingService.findByTrainerUsername("testUser", fromDate, toDate, null)).thenReturn(mockTrainings);
//
//        ResponseEntity<?> response = trainingController.getTrainerTrainingList(mockPrincipal, fromDate, toDate, null);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockTrainings, response.getBody());
//        verify(trainingService, times(1)).findByTrainerUsername("testUser", fromDate, toDate, null);
//    }

    @Test
    void testAddTraining_Success() throws Exception {
        CreateTrainingDTO createTrainingDTO = new CreateTrainingDTO();
        createTrainingDTO.setTraineeUsername("testTrainee");
        createTrainingDTO.setTrainerUsername("testTrainer");
        createTrainingDTO.setDate(LocalDate.now());
        createTrainingDTO.setDuration(60);

        Trainer mockTrainer = new Trainer();
        mockTrainer.setFirstName("Test");
        mockTrainer.setLastName("Trainer");

        Trainee mockTrainee = new Trainee();
        mockTrainee.setFirstName("Test");
        mockTrainee.setLastName("Trainee");

        TrainingType mockTrainingType = new TrainingType();
        mockTrainingType.setTrainingTypeName(TrainingTypeEnum.ZUMBA);

        Training mockTraining = new Training();
        mockTraining.setId(1L);
        mockTraining.setTrainingName("Morning ZUMBA Training");
        mockTraining.setTrainingDate(LocalDate.now());
        mockTraining.setDuration(60);
        mockTraining.setTrainer(mockTrainer);
        mockTraining.setTrainee(mockTrainee);
        mockTraining.setTrainingType(mockTrainingType);

        when(trainingService.addTraining(createTrainingDTO)).thenReturn(mockTraining);

        ResponseEntity<?> response = trainingController.addTraining(mockPrincipal, createTrainingDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(trainingService, times(1)).addTraining(createTrainingDTO);
    }

    @Test
    void testAddTraining_Failure() throws Exception {
        CreateTrainingDTO createTrainingDTO = new CreateTrainingDTO();
        String errorMessage = "Failed to add training";

        when(trainingService.addTraining(createTrainingDTO)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> response = trainingController.addTraining(mockPrincipal, createTrainingDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(trainingService, times(1)).addTraining(createTrainingDTO);
    }
}
