package com.example.springpr.gymapp.mapperTest;


import com.example.springpr.gymapp.dto.TrainingTypeDTO;
import com.example.springpr.gymapp.mapper.TrainingTypeMapper;
import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.model.TrainingTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingTypeMapperTest {

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName(TrainingTypeEnum.YOGA);
    }

    @Test
    void testToDTO() {
        TrainingTypeDTO trainingTypeDTO = TrainingTypeMapper.toDTO(trainingType);

        assertEquals(trainingType.getId(), trainingTypeDTO.getId());
        assertEquals(trainingType.getTrainingTypeName().name(), trainingTypeDTO.getName());
    }
}