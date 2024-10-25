package com.example.springpr.gymapp.mapperTest;

import com.example.springpr.gymapp.dto.Trainee.ShortTraineeDTO;
import com.example.springpr.gymapp.dto.Trainee.TraineeDTO;
import com.example.springpr.gymapp.mapper.TraineeMapper;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.model.TrainingTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TraineeMapperTest {

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeEnum.YOGA);

        trainer = new Trainer();
        trainer.setUsername("trainer.test");
        trainer.setFirstName("TrainerFirst");
        trainer.setLastName("TrainerLast");
        trainer.setSpecialization(trainingType);

        trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setActive(true);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("123 Street");
        trainee.setTrainers(Collections.singletonList(trainer));
        trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName());
    }

    @Test
    void testToDTO_WithTrainerList() {
        TraineeDTO traineeDTO = TraineeMapper.toDTO(trainee, true);

        assertEquals("John.Doe", traineeDTO.getUsername());
        assertEquals("John", traineeDTO.getFirstName());
        assertEquals("Doe", traineeDTO.getLastName());
        assertEquals(true, traineeDTO.isActive());
        assertEquals(LocalDate.of(1990, 1, 1), traineeDTO.getDateOfBirth());
        assertEquals("123 Street", traineeDTO.getAddress());
        assertEquals(1, traineeDTO.getTrainers().size());
        assertEquals("trainer.test", traineeDTO.getTrainers().get(0).getUsername());
        assertEquals("YOGA", traineeDTO.getTrainers().get(0).getSpecialization());
    }

    @Test
    void testToDTO_WithoutTrainerList() {
        TraineeDTO traineeDTO = TraineeMapper.toDTO(trainee, false);

        assertEquals("John.Doe", traineeDTO.getUsername());
        assertEquals("John", traineeDTO.getFirstName());
        assertEquals("Doe", traineeDTO.getLastName());
        assertEquals(true, traineeDTO.isActive());
        assertEquals(LocalDate.of(1990, 1, 1), traineeDTO.getDateOfBirth());
        assertEquals("123 Street", traineeDTO.getAddress());
        assertNull(traineeDTO.getTrainers());
    }

    @Test
    void testToShortDTO() {
        ShortTraineeDTO shortTraineeDTO = TraineeMapper.toShortDTO(trainee);

        assertEquals("John.Doe", shortTraineeDTO.getUsername());
        assertEquals("John", shortTraineeDTO.getFirstName());
        assertEquals("Doe", shortTraineeDTO.getLastName());
    }
}