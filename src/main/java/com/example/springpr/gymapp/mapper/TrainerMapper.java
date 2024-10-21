package com.example.springpr.gymapp.mapper;

import com.example.springpr.gymapp.dto.TrainerDTO;
import com.example.springpr.gymapp.model.Trainer;

public class TrainerMapper {
    public static TrainerDTO toDTO(Trainer trainer) {
        return new TrainerDTO(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                trainer.getSpecialization().getTrainingTypeName().name()
        );
    }
}
