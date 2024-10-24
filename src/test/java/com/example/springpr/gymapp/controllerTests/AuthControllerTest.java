package com.example.springpr.gymapp.controllerTests;

import com.example.springpr.gymapp.controller.AuthController;
import com.example.springpr.gymapp.dto.Trainee.SignupTrainee;
import com.example.springpr.gymapp.dto.Trainer.SignupTrainer;
import com.example.springpr.gymapp.model.*;
import com.example.springpr.gymapp.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.springpr.gymapp.dto.*;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.TrainerService;
import com.example.springpr.gymapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createAuthToken_shouldReturnOk_whenAuthRequestIsValid() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("password");

        String mockToken = "mockJwtToken";
        when(authService.createAuthToken(any())).thenReturn(ResponseEntity.ok(mockToken));

        mockMvc.perform(post("/auth")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(mockToken));
    }

    // signupTrainee

    @Test
    void signupTrainee_shouldReturnCreated_whenTraineeSignupIsSuccessful() throws Exception {
        SignupTrainee traineeDTO = new SignupTrainee();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setDateOfBirth(LocalDate.of(2005, 3, 11));
        traineeDTO.setAddress("address1");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(LocalDate.of(2005, 3, 11));
        trainee.setAddress("address1");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("John.Doe");
        userDTO.setPassword("password");

        when(traineeService.mapToEntity(traineeDTO)).thenReturn(trainee);
        when(authService.signUpTrainee(eq(trainee))).thenReturn(Optional.of(userDTO));

        String jsonContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"dateOfBirth\": \"2005-03-11\", \"address\": \"address1\" }";

        mockMvc.perform(post("/signup/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void signupTrainee_shouldReturnBadRequest_whenFirstNameOrLastNameIsMissing() throws Exception {
        SignupTrainee traineeDTO = new SignupTrainee();
        traineeDTO.setLastName("Doe");

        mockMvc.perform(post("/signup/trainee")
                        .contentType("application/json")
                        .content("{\"lastName\":\"Doe\"}"))
                .andExpect(status().isBadRequest());

        traineeDTO = new SignupTrainee();
        traineeDTO.setFirstName("John");

        mockMvc.perform(post("/signup/trainee")
                        .contentType("application/json")
                        .content("{\"firstName\":\"John\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signupTrainee_shouldReturnBadRequest_whenCreatedUserIsntPresent() throws Exception {
        SignupTrainee traineeDTO = new SignupTrainee();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName("Doe");
        traineeDTO.setDateOfBirth(LocalDate.of(2005, 3, 11));
        traineeDTO.setAddress("address1");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(LocalDate.of(2005, 3, 11));
        trainee.setAddress("address1");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("John.Doe");
        userDTO.setPassword("password");

        when(traineeService.mapToEntity(traineeDTO)).thenReturn(trainee);
        when(authService.signUpTrainee(eq(trainee))).thenReturn(Optional.empty());

        String jsonContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"dateOfBirth\": \"2005-03-11\", \"address\": \"address1\" }";
        mockMvc.perform(post("/signup/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    // signupTrainer

    @Test
    void signupTrainer_shouldReturnCreated_whenTrainerSignupIsSuccessful() throws Exception {
        SignupTrainer trainerDTO = new SignupTrainer();
        trainerDTO.setFirstName("Jane");
        trainerDTO.setLastName("Doe");
        trainerDTO.setSpecialization("YOGA");

        Trainer trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");
        TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.YOGA;
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeEnum);
        trainer.setSpecialization(trainingType);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Jane.Doe");
        userDTO.setPassword("password");

        when(trainerService.mapToEntity(any(SignupTrainer.class))).thenReturn(trainer);
        when(authService.signUpTrainer(eq(trainer))).thenReturn(Optional.of(userDTO));

        String jsonContent = "{ \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"specialization\": \"YOGA\" }";

        mockMvc.perform(post("/signup/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Jane.Doe"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void signupTrainer_shouldReturnBadRequest_whenFirstNameIsMissing() throws Exception {
        SignupTrainer trainerDTO = new SignupTrainer();
        trainerDTO.setLastName("Doe");
        trainerDTO.setSpecialization("YOGA");

        mockMvc.perform(post("/signup/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Doe\", \"specialization\":\"YOGA\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("First name length must be at least 1 character"));
    }

    @Test
    void signupTrainer_shouldReturnBadRequest_whenLastNameIsMissing() throws Exception {
        SignupTrainer trainerDTO = new SignupTrainer();
        trainerDTO.setFirstName("Jane");
        trainerDTO.setSpecialization("YOGA");

        mockMvc.perform(post("/signup/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Jane\", \"specialization\":\"YOGA\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Last name length must be at least 1 character"));
    }

    @Test
    void signupTrainer_shouldReturnBadRequest_whenCreatedUserIsntPresent() throws Exception {
        SignupTrainer trainerDTO = new SignupTrainer();
        trainerDTO.setFirstName("Jane");
        trainerDTO.setLastName("Doe");
        trainerDTO.setSpecialization("YOGA");

        Trainer trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");

        // Устанавливаем специализацию
        TrainingTypeEnum trainingTypeEnum = TrainingTypeEnum.YOGA;
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeEnum);
        trainer.setSpecialization(trainingType);

        when(trainerService.mapToEntity(any(SignupTrainer.class))).thenReturn(trainer);
        when(authService.signUpTrainer(eq(trainer))).thenReturn(Optional.empty());

        String jsonContent = "{ \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"specialization\": \"YOGA\" }";

        mockMvc.perform(post("/signup/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Something went wrong"));
    }

    //changeLoginTrainee

    @Test
    void changeLoginTrainee_shouldReturnOk_whenPasswordChangeIsSuccessful() throws Exception {
        String username = "test.User";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        Principal principal = () -> username;

        when(userService.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(authService.changePassword(username, oldPassword, newPassword)).thenReturn(true);

        mockMvc.perform(put("/trainee/changeLogin")
                        .principal(principal)  // Устанавливаем Principal
                        .contentType("application/json")
                        .content("{\"oldPassword\":\"" + oldPassword + "\",\"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isOk());
    }

    //changeLoginTrainee
    @Test
    void changeLoginTrainer_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        String username = "nonExistingUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        Principal principal = () -> username;

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(put("/trainer/changeLogin")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"oldPassword\":\"" + oldPassword + "\",\"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeLogin_shouldReturnBadRequest_whenPasswordChangeFails() throws Exception {
        String username = "test.User";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        Principal principal = () -> username;

        when(userService.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(authService.changePassword(username, oldPassword, newPassword)).thenReturn(false);

        mockMvc.perform(put("/trainee/changeLogin")
                        .principal(principal)
                        .contentType("application/json")
                        .content("{\"oldPassword\":\"" + oldPassword + "\",\"newPassword\":\"" + newPassword + "\"}"))
                .andExpect(status().isBadRequest());
    }
}