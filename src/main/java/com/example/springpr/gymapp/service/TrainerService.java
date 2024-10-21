package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.TrainerDTO;
import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.model.TrainingTypeEnum;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public Trainer mapToEntity(TrainerDTO trainerDTO) {
        if (trainerDTO == null) {
            return null;
        }
        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerDTO.getFirstName());
        trainer.setLastName(trainerDTO.getLastName());

        TrainingTypeEnum specializationEnum;
        try {
            specializationEnum = TrainingTypeEnum.valueOf(trainerDTO.getSpecialization().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid specialization type: " + trainerDTO.getSpecialization());
        }

        TrainingType trainingType = trainingTypeRepository
                .findByTrainingTypeName(specializationEnum)
                .orElseThrow(() -> new RuntimeException("TrainingType not found for specialization: " + specializationEnum));

        trainer.setSpecialization(trainingType);

        return trainer;
    }
}
