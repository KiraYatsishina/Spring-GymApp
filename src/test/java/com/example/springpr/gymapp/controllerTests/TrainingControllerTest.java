package com.example.springpr.gymapp.controllerTests;

import com.example.springpr.gymapp.controller.TrainingController;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createTraineeTest() throws Exception {
        Training training = new Training(1L, 1L, 1L, "Yoga Training", "Yoga", LocalDate.of(2024, 10, 1), 60);
        String trainingJson = objectMapper.writeValueAsString(training);

        mockMvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingJson))
                .andExpect(status().isOk());

        verify(trainingService, times(1)).createTraining(training);
    }

    @Test
    void getTraineeByIdTest() throws Exception {
        Training expectedTraining = new Training(1L, 1L, 1L, "Yoga Training", "Yoga", LocalDate.of(2024, 10, 1), 60);
        when(trainingService.getTrainingById(1L)).thenReturn(expectedTraining);

        mockMvc.perform(get("/trainings/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedTraining.getId()))
                .andExpect(jsonPath("$.traineeId").value(expectedTraining.getTraineeId()))
                .andExpect(jsonPath("$.trainerId").value(expectedTraining.getTrainerId()))
                .andExpect(jsonPath("$.trainingName").value(expectedTraining.getTrainingName()))
                .andExpect(jsonPath("$.trainingType.trainingTypeName").value(expectedTraining.getTrainingType().getTrainingTypeName()))
                .andExpect(jsonPath("$.trainingDate").value(expectedTraining.getTrainingDate().toString()))
                .andExpect(jsonPath("$.trainingDuration").value(expectedTraining.getTrainingDuration()));

        verify(trainingService, times(1)).getTrainingById(1L);
    }

    @Test
    void getAllTraineesTest() throws Exception {
        List<Training> trainings = Arrays.asList(
                new Training(1L, 1L, 1L, "Yoga Training", "Yoga", LocalDate.of(2024, 10, 1), 60),
                new Training(2L, 2L, 1L, "Cardio Training", "Cardio", LocalDate.of(2024, 10, 2), 45)
        );

        when(trainingService.getAllTrainings()).thenReturn(trainings);

        mockMvc.perform(get("/trainings/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", contains(1, 2)))
                .andExpect(jsonPath("$[*].traineeId", contains(1, 2)))
                .andExpect(jsonPath("$[*].trainerId", contains(1, 1)))
                .andExpect(jsonPath("$[*].trainingName", contains("Yoga Training", "Cardio Training")))
                .andExpect(jsonPath("$[*].trainingType.trainingTypeName", contains("Yoga", "Cardio")))
                .andExpect(jsonPath("$[*].trainingDate", contains("2024-10-01", "2024-10-02")))
                .andExpect(jsonPath("$[*].trainingDuration", contains(60, 45)));

        verify(trainingService, times(1)).getAllTrainings();
    }
}
