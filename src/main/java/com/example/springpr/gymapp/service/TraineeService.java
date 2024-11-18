package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.Trainer.ShortTrainerDTO;
import com.example.springpr.gymapp.dto.Trainee.SignupTrainee;
import com.example.springpr.gymapp.dto.Trainee.TraineeDTO;
import com.example.springpr.gymapp.dto.Trainee.UpdateTraineeDTO;
import com.example.springpr.gymapp.mapper.TraineeMapper;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.repository.TraineeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeRepository traineeRepository;

    public Optional<TraineeDTO> findByUsername(String username) {
        Optional<Trainee> trainee = traineeRepository.findByUsername(username);
        return trainee.map(t -> TraineeMapper.toDTO(t, true));
    }

    public Trainee mapToEntity(SignupTrainee traineeDTO) {
        if (traineeDTO == null) {
            logger.warn("SignupTrainee DTO is null");
            return null;
        }
        Trainee trainee = new Trainee();
        trainee.setFirstName(traineeDTO.getFirstName());
        trainee.setLastName(traineeDTO.getLastName());
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        trainee.setAddress(traineeDTO.getAddress());
        return trainee;
    }

    @Transactional
    public Optional<Trainee> updateTraineeProfile(String username, UpdateTraineeDTO updateTraineeDTO) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUsername(username);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();

            trainee.setFirstName(updateTraineeDTO.getFirstName());
            trainee.setLastName(updateTraineeDTO.getLastName());
            trainee.setDateOfBirth(updateTraineeDTO.getDateOfBirth());
            trainee.setAddress(updateTraineeDTO.getAddress());
            trainee.setActive(updateTraineeDTO.isActive());
            return Optional.of(traineeRepository.save(trainee));
        }
        return Optional.empty();
    }

    @Transactional
    public List<ShortTrainerDTO> getNotAssignedTrainersList(String username){
        Optional<Trainee> traineeOptional = traineeRepository.findByUsername(username);
        List<Trainer> notAssignedTrainers = traineeRepository.findNotAssignedTrainers(traineeOptional.get().getUserId());

        return notAssignedTrainers.stream()
                .map(TrainerMapper::toShortDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<ShortTrainerDTO> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));

        List<Trainer> trainers = traineeRepository.findByUsernameIn(trainerUsernames);

        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);

        return trainers.stream()
                .map(TrainerMapper::toShortDTO).collect(Collectors.toList());
    }

    public long countActiveTrainees() {
        return traineeRepository.countByIsActive(true);
    }
}
