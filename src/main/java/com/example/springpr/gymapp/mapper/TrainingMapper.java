package com.example.springpr.gymapp.mapper;

import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.model.Training;

import java.util.stream.Collectors;

public class TrainingMapper {
    public static TrainingDTO toDTO(Training training, boolean isTrainee) {
        String personName = isTrainee ? training.getTrainer().getFullName() : training.getTrainee().getFullName();

        return new TrainingDTO(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainingType().getTrainingTypeName().name(),
                training.getDuration(),
                personName
        );
    }
}
