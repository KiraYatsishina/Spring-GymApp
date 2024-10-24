package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.ShortTrainerDTO;
import com.example.springpr.gymapp.dto.SignupTrainee;
import com.example.springpr.gymapp.dto.TraineeDTO;
import com.example.springpr.gymapp.dto.UpdateTraineeDTO;
import com.example.springpr.gymapp.mapper.TraineeMapper;
import com.example.springpr.gymapp.mapper.TrainerMapper;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.repository.TraineeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;

    public Optional<TraineeDTO> findByUsername(String username) {
        Optional<Trainee> trainee = traineeRepository.findByUsername(username);
        return trainee.map(t -> TraineeMapper.toDTO(t, true));
    }

    public Trainee mapToEntity(SignupTrainee traineeDTO) {
        if (traineeDTO == null) {
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
}
