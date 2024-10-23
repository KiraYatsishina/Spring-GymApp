package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.SignupTrainer;
import com.example.springpr.gymapp.dto.TrainerDTO;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.*;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;


    public Optional<TrainerDTO> findByUsername(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        return trainer.map(t -> TrainerMapper.toDTO(t, true));
    }

    public Trainer mapToEntity(SignupTrainer trainerDTO) {
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
