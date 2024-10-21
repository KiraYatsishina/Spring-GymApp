package com.example.springpr.gymapp.repository;

import com.example.springpr.gymapp.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long>{
    Optional<Trainee> findByUsername(String username);
    Trainee save(Trainee trainee);
}
