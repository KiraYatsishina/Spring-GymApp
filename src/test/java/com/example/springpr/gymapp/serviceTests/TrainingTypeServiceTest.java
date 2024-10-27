package com.example.springpr.gymapp.serviceTests;

import com.example.springpr.gymapp.dto.TrainingTypeDTO;
import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.model.TrainingTypeEnum;
import com.example.springpr.gymapp.repository.TrainingTypeRepository;
import com.example.springpr.gymapp.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void testGetAllTrainingTypes_returnsTrainingTypeDTOList() {
        TrainingType type1 = new TrainingType();
        type1.setTrainingTypeName(TrainingTypeEnum.FITNESS);

        TrainingType type2 = new TrainingType();
        type2.setTrainingTypeName(TrainingTypeEnum.YOGA);

        when(trainingTypeRepository.findAll()).thenReturn(Arrays.asList(type1, type2));

        List<TrainingTypeDTO> result = trainingTypeService.getAllTrainingTypes();

        assertEquals(2, result.size());
        assertEquals("FITNESS", result.get(0).getName());
        assertEquals("YOGA", result.get(1).getName());
    }
}
