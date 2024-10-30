package com.example.springpr.gymapp.controllerTests;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.controller.TraineeController;
import com.example.springpr.gymapp.dto.Trainee.TraineeDTO;
import com.example.springpr.gymapp.dto.Trainee.UpdateTraineeDTO;
import com.example.springpr.gymapp.dto.Trainer.ShortTrainerDTO;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private JwtCore jwtCore;

    @InjectMocks
    private TraineeController traineeController;

    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");
    }

    @Test
    void testGetMyProfile_Success() {
        TraineeDTO mockTraineeDTO = new TraineeDTO();
        when(traineeService.findByUsername("testUser")).thenReturn(Optional.of(mockTraineeDTO));

        ResponseEntity<TraineeDTO> response = traineeController.getMyProfile(mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTraineeDTO, response.getBody());
        verify(traineeService, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetMyProfile_NotFound() {
        when(traineeService.findByUsername("testUser")).thenReturn(Optional.empty());

        ResponseEntity<TraineeDTO> response = traineeController.getMyProfile(mockPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(traineeService, times(1)).findByUsername("testUser");
    }

    @Test
    void testUpdateTraineeProfile_Success() {
        UpdateTraineeDTO updateTraineeDTO = new UpdateTraineeDTO();
        updateTraineeDTO.setFirstName("John");
        updateTraineeDTO.setLastName("Doe");

        Trainee mockTrainee = new Trainee();
        mockTrainee.setUsername("testUser");

        when(traineeService.updateTraineeProfile("testUser", updateTraineeDTO)).thenReturn(Optional.of(mockTrainee));

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername("testUser")).thenReturn(mockUserDetails);

        ResponseEntity<?> response = traineeController.updateTraineeProfile(mockPrincipal, updateTraineeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(traineeService, times(1)).updateTraineeProfile("testUser", updateTraineeDTO);
    }

    @Test
    void testUpdateTraineeProfile_MissingFirstName() {
        UpdateTraineeDTO updateTraineeDTO = new UpdateTraineeDTO();
        updateTraineeDTO.setFirstName("");
        updateTraineeDTO.setLastName("Doe");

        ResponseEntity<?> response = traineeController.updateTraineeProfile(mockPrincipal, updateTraineeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("First Name is required", response.getBody());
    }

    @Test
    void testUpdateTraineeProfile_MissingLastName() {
        UpdateTraineeDTO updateTraineeDTO = new UpdateTraineeDTO();

        updateTraineeDTO.setFirstName("Doe");
        updateTraineeDTO.setLastName("");

        ResponseEntity<?> response = traineeController.updateTraineeProfile(mockPrincipal, updateTraineeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Last Name is required", response.getBody());
    }

    @Test
    void testGetNotAssignedTrainersList_Success() {
        List<ShortTrainerDTO> mockTrainersList = new ArrayList<>();
        when(traineeService.findByUsername("testUser")).thenReturn(Optional.of(new TraineeDTO()));
        when(traineeService.getNotAssignedTrainersList("testUser")).thenReturn(mockTrainersList);

        ResponseEntity<List<ShortTrainerDTO>> response = traineeController.getNotAssignedTrainersList(mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrainersList, response.getBody());
        verify(traineeService, times(1)).getNotAssignedTrainersList("testUser");
    }

    @Test
    void testGetNotAssignedTrainersList_TraineeNotFound() {
        when(traineeService.findByUsername("testUser")).thenReturn(Optional.empty());

        ResponseEntity<List<ShortTrainerDTO>> response = traineeController.getNotAssignedTrainersList(mockPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(traineeService, times(0)).getNotAssignedTrainersList("testUser");
    }

    @Test
    void testUpdateTrainersList_Success() {
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");
        List<ShortTrainerDTO> updatedTrainers = new ArrayList<>();
        when(traineeService.updateTraineeTrainers("testUser", trainerUsernames)).thenReturn(updatedTrainers);

        ResponseEntity<?> response = traineeController.updateTrainersList(mockPrincipal, trainerUsernames);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTrainers, response.getBody());
        verify(traineeService, times(1)).updateTraineeTrainers("testUser", trainerUsernames);
    }

    @Test
    void testUpdateTraineeProfile_Failure() {
        UpdateTraineeDTO updateTraineeDTO = new UpdateTraineeDTO();
        updateTraineeDTO.setFirstName("John");
        updateTraineeDTO.setLastName("Doe");

        when(traineeService.updateTraineeProfile("testUser", updateTraineeDTO)).thenReturn(Optional.empty());

        ResponseEntity<?> response = traineeController.updateTraineeProfile(mockPrincipal, updateTraineeDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainee not found", response.getBody());
        verify(traineeService, times(1)).updateTraineeProfile("testUser", updateTraineeDTO);
    }
}
