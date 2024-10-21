package com.example.springpr.gymapp.repository;

import com.example.springpr.gymapp.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
