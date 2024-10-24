package com.example.springpr.gymapp.repository;

import com.example.springpr.gymapp.model.TrainingType;
import com.example.springpr.gymapp.model.TrainingTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findByTrainingTypeName(TrainingTypeEnum trainingTypeEnum);
    List<TrainingType> findAll();
}
