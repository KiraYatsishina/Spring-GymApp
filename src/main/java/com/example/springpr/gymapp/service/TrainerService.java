package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.Trainer.SignupTrainer;
import com.example.springpr.gymapp.dto.Trainer.TrainerDTO;
import com.example.springpr.gymapp.dto.Trainer.UpdateTrainerDTO;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.*;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class); // Logger for tracking actions

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public Optional<TrainerDTO> findByUsername(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUsername(username);
        return trainer.map(t -> TrainerMapper.toDTO(t, true));
    }

    public Trainer mapToEntity(SignupTrainer trainerDTO) {
        if (trainerDTO == null) {
            logger.warn("SignupTrainer DTO is null");
            return null;
        }
        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerDTO.getFirstName());
        trainer.setLastName(trainerDTO.getLastName());

        TrainingTypeEnum specializationEnum;
        try {
            specializationEnum = TrainingTypeEnum.valueOf(trainerDTO.getSpecialization().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid specialization type: {}", trainerDTO.getSpecialization());
            throw new RuntimeException("Invalid specialization type: " + trainerDTO.getSpecialization());
        }

        TrainingType trainingType = trainingTypeRepository
                .findByTrainingTypeName(specializationEnum)
                .orElseThrow(() -> new RuntimeException("TrainingType not found for specialization: " + specializationEnum));

        trainer.setSpecialization(trainingType);

        return trainer;
    }

    @Transactional
    public Optional<Trainer> updateTrainerProfile(String username, UpdateTrainerDTO updateTrainerDTO) {
        Optional<Trainer> trainerOptional = trainerRepository.findByUsername(username);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.setFirstName(updateTrainerDTO.getFirstName());
            trainer.setLastName(updateTrainerDTO.getLastName());

            TrainingTypeEnum specializationEnum;
            try {
                specializationEnum = TrainingTypeEnum.valueOf(updateTrainerDTO.getSpecialization().toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid specialization type provided for username {}: {}", username, updateTrainerDTO.getSpecialization());
                return Optional.empty();
            }

            Optional<TrainingType> specialization = trainingTypeRepository.findByTrainingTypeName(specializationEnum);
            if (specialization.isPresent()) {
                trainer.setSpecialization(specialization.get());
            } else {
                logger.warn("Specialization not found for username {}: {}", username, specializationEnum);
                return Optional.empty();
            }

            trainer.setActive(updateTrainerDTO.isActive());
            return Optional.of(trainerRepository.save(trainer));
        }
        return Optional.empty();
    }
}
