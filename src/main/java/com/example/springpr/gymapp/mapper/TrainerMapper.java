package com.example.springpr.gymapp.mapper;

import com.example.springpr.gymapp.dto.ShortTrainerDTO;
import com.example.springpr.gymapp.dto.TrainerDTO;
import com.example.springpr.gymapp.model.Trainer;

import java.util.stream.Collectors;

public class TrainerMapper {
    public static TrainerDTO toDTO(Trainer trainer, boolean hasTraineeList) {
        return new TrainerDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                trainer.getSpecialization().getTrainingTypeName().name(),
                hasTraineeList ? trainer.getTrainees().stream()
                        .map(TraineeMapper::toShortDTO)
                        .collect(Collectors.toList()) : null
        );
    }

    public static ShortTrainerDTO toShortDTO(Trainer trainer) {
        return new ShortTrainerDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization().getTrainingTypeName().name()
        );
    }
}
