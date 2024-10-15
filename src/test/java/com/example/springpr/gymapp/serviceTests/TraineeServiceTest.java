package com.example.springpr.gymapp.serviceTests;

import com.example.springpr.gymapp.CountHelper;
import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
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
public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private CountHelper countHelper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createTraineeTest() {
        Trainee trainee = new Trainee(1L, "Kira", "Yatsishina", 0L,
                true, LocalDate.of(1988, 3, 3), "Odesa");

        traineeService.createTrainee(trainee);

        verify(traineeDAO, times(1)).createTrainee(trainee);
    }

    @Test
    void getTraineeByIdTest() {
        Long traineeId = 1L;
        Trainee expectedTrainee = new Trainee(traineeId, "Kira", "Yatsishina", 0L,
                true, LocalDate.of(1988, 3, 3), "Odesa");

        when(traineeDAO.getTraineeById(traineeId)).thenReturn(expectedTrainee);

        Trainee actualTrainee = traineeService.getTraineeById(traineeId);

        assertNotNull(actualTrainee);
        assertEquals(expectedTrainee.getId(), actualTrainee.getId());
        assertEquals(expectedTrainee.getFirstName(), actualTrainee.getFirstName());
        assertEquals(expectedTrainee.getLastName(), actualTrainee.getLastName());
        assertEquals(expectedTrainee.getUsername(), actualTrainee.getUsername());
        assertEquals(expectedTrainee.getPassword(), actualTrainee.getPassword());
        assertEquals(expectedTrainee.getIsActive(), actualTrainee.getIsActive());
        assertEquals(expectedTrainee.getDateOfBirth(), actualTrainee.getDateOfBirth());
        assertEquals(expectedTrainee.getAddress(), actualTrainee.getAddress());

        verify(traineeDAO, times(1)).getTraineeById(traineeId);
    }

    @Test
    void updateTraineeTest() {
        Trainee trainee = new Trainee(1L, "Kira", "Yatsishina", 0L,
                true, LocalDate.of(1988, 3, 3), "Odesa");

        long count = 2L;
        when(countHelper.countUser(trainee.getFirstName(), trainee.getLastName())).thenReturn(count);

        traineeService.updateTrainee(1L, trainee);

        verify(countHelper, times(1)).countUser(trainee.getFirstName(), trainee.getLastName());
        verify(traineeDAO, times(1)).updateTrainee(1L, trainee, count);
    }

    @Test
    void deleteTraineeTest() {
        Long traineeId = 1L;

        traineeService.deleteTrainee(traineeId);

        verify(traineeDAO, times(1)).deleteTrainee(traineeId);
    }

    @Test
    void getAllTraineesTest() {
        List<Trainee> trainees = Arrays.asList(
                new Trainee(1L, "Emily", "Jones", 0L,
                        false, LocalDate.of(1988, 3, 3), "address1"),
                new Trainee(2L, "Michael", "Brown", 0L,
                        true, LocalDate.of(1995, 4, 4), "address2"),
                new Trainee(3L, "Sarah", "Johnson", 0L,
                        false, LocalDate.of(1993, 5, 5), "address3")
        );

        when(traineeDAO.getAllTrainees()).thenReturn(trainees);

        List<Trainee> traineeList = traineeService.getAllTrainees();

        assertEquals(3, traineeList.size());
        verify(traineeDAO, times(1)).getAllTrainees();
    }
}
