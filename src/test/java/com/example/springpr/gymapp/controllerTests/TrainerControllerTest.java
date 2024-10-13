package com.example.springpr.gymapp.controllerTests;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springpr.gymapp.controller.TrainerController;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createTrainerTest() throws Exception {
        Trainer trainer = new Trainer(1L, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, "Yoga");
        String trainerJson = objectMapper.writeValueAsString(trainer);

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(trainerJson))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).createTrainer(trainer);
    }

    @Test
    void getTrainerByIdTest() throws Exception {
        Trainer expectedTrainer = new Trainer(1L, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, "Yoga");

        when(trainerController.getTrainer(1L)).thenReturn(expectedTrainer);

        mockMvc.perform(get("/trainers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedTrainer.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedTrainer.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedTrainer.getLastName()))
                .andExpect(jsonPath("$.isActive").value(expectedTrainer.getIsActive()))
                .andExpect(jsonPath("$.password").value(expectedTrainer.getPassword()))
                .andExpect(jsonPath("$.username").value(expectedTrainer.getUsername()))
                .andExpect(jsonPath("$.trainingType.trainingTypeName").value(expectedTrainer.getTrainingType().getTrainingTypeName()));

        verify(trainerService, times(1)).getTrainerById(1L);
    }

    @Test
    void updateTrainerTest() throws Exception {
        Trainer updatedTrainer = new Trainer(1L, "Kira", "NEWYatsishina", null,  null,
                true, "Yoga");

        doNothing().when(trainerService).updateTrainer(anyLong(), Mockito.any(Trainer.class));

        mockMvc.perform(put("/trainers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTrainer)))
                .andExpect(status().isNoContent());

        verify(trainerService, times(1)).updateTrainer(eq(1L), Mockito.any(Trainer.class));

    }

    @Test
    void deleteTrainerTest() throws Exception {
        Trainer trainer = new Trainer(1L, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, "Yoga");

        doNothing().when(trainerService).deleteTrainer(1L);
        mockMvc.perform(delete("/trainers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isOk());

        verify(trainerService, times(1)).deleteTrainer(1L);

    }

    @Test
    void getAllTrainers() throws Exception {
        when(trainerService.getAllTrainers()).thenReturn(Arrays.asList(
                new Trainer(1L, "John", "Smith", "John.Smith", "password123",
                        true, "Personal Trainer"),
                new Trainer(2L, "Jane", "Doe", "Jane.Doe", "password456",
                        true, "Strength Coach"),
                new Trainer(3L, "Bob", "Brown", "Bob.Brown", "password789",
                        true,"Yoga Instructor")
        ));

        mockMvc.perform(get("/trainers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].id", contains(1, 2, 3)))
                .andExpect(jsonPath("$[*].firstName", contains("John", "Jane", "Bob")))
                .andExpect(jsonPath("$[*].lastName", contains("Smith", "Doe", "Brown")))
                .andExpect(jsonPath("$[*].username", contains("John.Smith", "Jane.Doe", "Bob.Brown")))
                .andExpect(jsonPath("$[*].password", contains("password123", "password456", "password789")))
                .andExpect(jsonPath("$[*].isActive", contains(true, true, true)))
                .andExpect(jsonPath("$[*].trainingType.trainingTypeName", contains("Personal Trainer", "Strength Coach", "Yoga Instructor")));

        verify(trainerService, times(1)).getAllTrainers();
    }

}
