package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.CreateTrainingDTO;
import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.mapper.TrainingMapper;
import com.example.springpr.gymapp.model.*;
import com.example.springpr.gymapp.repository.TraineeRepository;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.TrainingRepository;
import com.example.springpr.gymapp.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public List<TrainingDTO> findByTraineeUsername(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        return trainingRepository.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingType)
                .stream()
                .map(training -> TrainingMapper.toDTO(training, true))
                .collect(Collectors.toList());
    }

    public List<TrainingDTO> findByTrainerUsername(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        return trainingRepository.findTrainerTrainings(username, fromDate, toDate, traineeName)
                .stream()
                .map(training -> TrainingMapper.toDTO(training, false))
                .collect(Collectors.toList());
    }

    public Training addTraining(CreateTrainingDTO request) throws Exception {
        Trainee trainee = traineeRepository.findByUsername(request.getTraineeUsername())
                .orElseThrow(() -> new Exception("Trainee not found"));

        Trainer trainer = trainerRepository.findByUsername(request.getTrainerUsername())
                .orElseThrow(() -> new Exception("Trainer not found"));

        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(TrainingTypeEnum.valueOf(request.getType()))
                .orElseThrow(() -> new Exception("Training type not found"));

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(request.getName());
        training.setTrainingDate(request.getDate());
        training.setTrainingType(trainingType);
        training.setDuration(request.getDuration());

        Training savedTraining = trainingRepository.save(training);
        return savedTraining;
    }
}
