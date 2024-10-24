package com.example.springpr.gymapp.mapper;

import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.dto.TrainingTypeDTO;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.model.TrainingType;

public class TrainingTypeMapper {
    public static TrainingTypeDTO toDTO(TrainingType trainingType) {
        return new TrainingTypeDTO(
                trainingType.getId(),
                trainingType.getTrainingTypeName().name()
        );
    }
}
