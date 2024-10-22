package com.example.springpr.gymapp.mapper;


import com.example.springpr.gymapp.dto.ShortTraineeDTO;
import com.example.springpr.gymapp.dto.TraineeDTO;
import com.example.springpr.gymapp.model.Trainee;

import java.util.stream.Collectors;

public class TraineeMapper {
    public static TraineeDTO toDTO(Trainee trainee, boolean hasTrainerList) {
        return new TraineeDTO(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.isActive(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                hasTrainerList ? trainee.getTrainers().stream()
                        .map(TrainerMapper::toShortDTO)
                        .collect(Collectors.toList()) : null
        );
    }

    public static ShortTraineeDTO toShortDTO(Trainee trainee) {
        return new ShortTraineeDTO(
                trainee.getUsername(),
                trainee.getFirstName(),
                trainee.getLastName()
        );
    }
}