package com.example.springpr.gymapp.repository;

import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long>{
    Optional<Trainee> findByUsername(String username);

    Trainee save(Trainee trainee);

    @Query("SELECT COUNT(u) FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    long countByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT t FROM Trainer t WHERE t NOT IN (SELECT tr FROM Trainee tra JOIN tra.trainers tr WHERE tra.userId = :userId)")
    List<Trainer> findNotAssignedTrainers(@Param("userId") Long userId);
}
