package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.TrainingService;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;
    private final UserService userService;

    @GetMapping("trainee/trainingList")
    public ResponseEntity<?> getTraineeTrainingList(Principal principal) {
        return getUserTrainingList(principal, Role.TRAINEE);
    }

    @GetMapping("trainer/trainingList")
    public ResponseEntity<?> getTrainerTrainingList(Principal principal) {
        return getUserTrainingList(principal, Role.TRAINER);
    }

    private ResponseEntity<?> getUserTrainingList(Principal principal, Role role) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if(userOptional.get().getRole() != role) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have permission");

       List<TrainingDTO> trainings = role.equals(Role.TRAINEE) ? trainingService.findByTraineeUsername(username) : trainingService.findByTrainerUsername(username);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }
}