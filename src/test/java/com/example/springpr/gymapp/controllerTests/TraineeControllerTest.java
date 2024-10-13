package com.example.springpr.gymapp.controllerTests;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springpr.gymapp.controller.TraineeController;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
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

import java.time.LocalDate;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class) // or @WebMcvTest(TraineeController.class)
@SpringBootTest
public class TraineeControllerTest {

     @Mock
     private TraineeService traineeService;

     @InjectMocks
     private TraineeController traineeController;

     private MockMvc mockMvc;
     private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createTraineeTest() throws Exception {
        Trainee trainee = new Trainee(1L, "Kira", "Yatsishina", "Kira.Yatsishina",  "myPassword",
                true, LocalDate.of(1988, 3, 3), "Odesa");
        String traineeJson = objectMapper.writeValueAsString(trainee);

        mockMvc.perform(post("/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(traineeJson))
                .andExpect(status().isOk());

        verify(traineeService, times(1)).createTrainee(trainee);
    }

    @Test
    void getTraineeByIdTest() throws Exception {
        Trainee expectedTrainee = new Trainee(1L, "Kira", "Yatsishina", "Kira.Yatsishina", "myPassword",
                true, LocalDate.of(1988, 3, 3), "Odesa");

        when(traineeService.getTraineeById(1L)).thenReturn(expectedTrainee);

        mockMvc.perform(get("/trainees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedTrainee.getId()))
                .andExpect(jsonPath("$.firstName").value(expectedTrainee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedTrainee.getLastName()))
                .andExpect(jsonPath("$.isActive").value(expectedTrainee.getIsActive()))
                .andExpect(jsonPath("$.password").value(expectedTrainee.getPassword()))
                .andExpect(jsonPath("$.username").value(expectedTrainee.getUsername()))
                .andExpect(jsonPath("$.dateOfBirth").value(expectedTrainee.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.address").value(expectedTrainee.getAddress()));

        verify(traineeService, times(1)).getTraineeById(1L);
    }

    @Test
    void updateTraineeTest() throws Exception {
        Trainee updatedTrainee = new Trainee(1L, "Kira", "Yatsishina", "Kira.Yatsishina", "newPassword",
                true, LocalDate.of(1988, 3, 3), "Kyiv");

        doNothing().when(traineeService).updateTrainee(anyLong(), Mockito.any(Trainee.class));

        mockMvc.perform(put("/trainees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTrainee)))
                .andExpect(status().isNoContent());

        verify(traineeService, times(1)).updateTrainee(eq(1L), Mockito.any(Trainee.class));
    }

    @Test
    void deleteTraineeTest() throws Exception {
        Trainee trainee = new Trainee(1L, "Kira", "Yatsishina", "Kira.Yatsishina", "myPassword",
                true, LocalDate.of(1988, 3, 3), "Odesa");

        doNothing().when(traineeService).deleteTrainee(1L);
        mockMvc.perform(delete("/trainees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainee)))
                .andExpect(status().isOk());

        verify(traineeService, times(1)).deleteTrainee(1L);
    }

    @Test
    void getAllTraineesTest() throws Exception {
        when(traineeService.getAllTrainees()).thenReturn(Arrays.asList(
                new Trainee(1L, "Emily", "Jones", "Emily.Jones", "password789",
                        false, LocalDate.of(1988, 3, 3), "address1"),
                new Trainee(2L, "Michael", "Brown", "Michael.Brown", "password101",
                        true, LocalDate.of(1995, 4, 4), "address2"),
                new Trainee(3L, "Sarah", "Johnson", "Sarah.Johnson", "password102",
                        false, LocalDate.of(1993, 5, 5), "address3")
        ));

        mockMvc.perform(get("/trainees/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].id", contains(1, 2, 3)))
                .andExpect(jsonPath("$[*].firstName", contains("Emily", "Michael", "Sarah")))
                .andExpect(jsonPath("$[*].lastName", contains("Jones", "Brown", "Johnson")))
                .andExpect(jsonPath("$[*].password", contains("password789", "password101", "password102")))
                .andExpect(jsonPath("$[*].username", contains("Emily.Jones", "Michael.Brown", "Sarah.Johnson")))
                .andExpect(jsonPath("$[*].isActive", contains(false, true, false)))
                .andExpect(jsonPath("$[*].dateOfBirth", contains("1988-03-03", "1995-04-04", "1993-05-05")))
                .andExpect(jsonPath("$[*].address", contains("address1", "address2", "address3")));

        verify(traineeService, times(1)).getAllTrainees();
    }
}
