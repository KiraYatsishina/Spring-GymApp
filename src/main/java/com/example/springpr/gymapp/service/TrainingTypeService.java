package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.dto.TrainingTypeDTO;
import com.example.springpr.gymapp.mapper.TrainingTypeMapper;
import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.repository.TrainingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;
    public List<TrainingTypeDTO> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        return trainingTypes.stream().map(TrainingTypeMapper::toDTO).collect(Collectors.toList());
    }
}
