package com.example.springpr.gymapp.mapper;


import com.example.springpr.gymapp.dto.TraineeDTO;
import com.example.springpr.gymapp.model.Trainee;

import java.util.stream.Collectors;

public class TraineeMapper {
    public static TraineeDTO toDTO(Trainee trainee) {
        return new TraineeDTO(
                trainee.getFirstName(),
                trainee.getLastName(),
                trainee.isActive(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getTrainers().stream()
                        .map(TrainerMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}