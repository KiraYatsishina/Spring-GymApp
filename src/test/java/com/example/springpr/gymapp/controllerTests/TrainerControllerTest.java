package com.example.springpr.gymapp.controllerTests;

import com.example.springpr.gymapp.controller.TrainerController;
import com.example.springpr.gymapp.dto.Trainer.TrainerDTO;
import com.example.springpr.gymapp.dto.Trainer.UpdateTrainerDTO;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.model.TrainingTypeEnum;
import com.example.springpr.gymapp.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testTrainer");
    }

    @Test
    void testGetMyProfile_Success() {
        TrainerDTO mockTrainerDTO = new TrainerDTO();
        when(trainerService.findByUsername("testTrainer")).thenReturn(Optional.of(mockTrainerDTO));

        ResponseEntity<TrainerDTO> response = trainerController.getMyProfile(mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainerDTO, response.getBody());
        verify(trainerService, times(1)).findByUsername("testTrainer");
    }

    @Test
    void testGetMyProfile_NotFound() {
        when(trainerService.findByUsername("testTrainer")).thenReturn(Optional.empty());

        ResponseEntity<TrainerDTO> response = trainerController.getMyProfile(mockPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(trainerService, times(1)).findByUsername("testTrainer");
    }

    @Test
    void testUpdateTrainerProfile_Success() {
        UpdateTrainerDTO updateTrainerDTO = new UpdateTrainerDTO();
        updateTrainerDTO.setFirstName("Jane");
        updateTrainerDTO.setLastName("Doe");
        updateTrainerDTO.setSpecialization("Yoga");

        Trainer mockTrainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeEnum.YOGA);
        mockTrainer.setSpecialization(trainingType);

        mockTrainer.setUsername("testTrainer");

        when(trainerService.updateTrainerProfile("testTrainer", updateTrainerDTO)).thenReturn(Optional.of(mockTrainer));

        ResponseEntity<?> response = trainerController.updateTrainerProfile(mockPrincipal, updateTrainerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(trainerService, times(1)).updateTrainerProfile("testTrainer", updateTrainerDTO);
    }

    @Test
    void testUpdateTrainerProfile_MissingFirstName() {
        UpdateTrainerDTO updateTrainerDTO = new UpdateTrainerDTO();
        updateTrainerDTO.setLastName("Doe");
        updateTrainerDTO.setSpecialization("Yoga");

        ResponseEntity<?> response = trainerController.updateTrainerProfile(mockPrincipal, updateTrainerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("First Name is required", response.getBody());
    }

    @Test
    void testUpdateTrainerProfile_MissingLastName() {
        UpdateTrainerDTO updateTrainerDTO = new UpdateTrainerDTO();
        updateTrainerDTO.setFirstName("Jane");
        updateTrainerDTO.setSpecialization("Yoga");

        ResponseEntity<?> response = trainerController.updateTrainerProfile(mockPrincipal, updateTrainerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Last Name is required", response.getBody());
    }

    @Test
    void testUpdateTrainerProfile_MissingSpecialization() {
        UpdateTrainerDTO updateTrainerDTO = new UpdateTrainerDTO();
        updateTrainerDTO.setFirstName("Jane");
        updateTrainerDTO.setLastName("Doe");

        ResponseEntity<?> response = trainerController.updateTrainerProfile(mockPrincipal, updateTrainerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Specialization is required", response.getBody());
    }

    @Test
    void testUpdateTrainerProfile_Failure() {
        UpdateTrainerDTO updateTrainerDTO = new UpdateTrainerDTO();
        updateTrainerDTO.setFirstName("Jane");
        updateTrainerDTO.setLastName("Doe");
        updateTrainerDTO.setSpecialization("Yoga");

        when(trainerService.updateTrainerProfile("testTrainer", updateTrainerDTO)).thenReturn(Optional.empty());

        ResponseEntity<?> response = trainerController.updateTrainerProfile(mockPrincipal, updateTrainerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Something with request", response.getBody());
        verify(trainerService, times(1)).updateTrainerProfile("testTrainer", updateTrainerDTO);
    }
}