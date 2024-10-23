package com.example.springpr.gymapp.repository;

import com.example.springpr.gymapp.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t WHERE t.trainee.username = :username")
    List<Training> findByTraineeUsername(@Param("username") String username);

    @Query("SELECT t FROM Training t WHERE t.trainer.username = :username")
    List<Training> findByTrainerUsername(@Param("username") String username);
}
