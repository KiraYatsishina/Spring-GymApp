package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.mapper.TrainingMapper;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;

    public List<TrainingDTO> findByTraineeUsername(String username) {
        List<Training> trainings = trainingRepository.findByTraineeUsername(username);
        return trainings.stream().map(training -> TrainingMapper.toDTO(training, true)).collect(Collectors.toList());
    }

    public List<TrainingDTO> findByTrainerUsername(String username) {
        List<Training> trainings = trainingRepository.findByTrainerUsername(username);
        return trainings.stream().map(training -> TrainingMapper.toDTO(training, false)).collect(Collectors.toList());
    }
}
