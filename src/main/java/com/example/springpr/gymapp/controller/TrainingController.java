package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.CreateTrainingDTO;
import com.example.springpr.gymapp.dto.TrainingDTO;
import com.example.springpr.gymapp.mapper.TrainingMapper;
import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.Training;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.TrainingService;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;
    private final UserService userService;

    @GetMapping("trainee/trainingList")
    public ResponseEntity<?> getTraineeTrainingList(Principal principal,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                    @RequestParam(required = false) String trainerName,
                                                    @RequestParam(required = false) String trainingType) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if(userOptional.get().getRole() != Role.TRAINEE) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have permission");
        List<TrainingDTO> trainings = trainingService.findByTraineeUsername(username, fromDate, toDate, trainerName, trainingType);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @GetMapping("trainer/trainingList")
    public ResponseEntity<?> getTrainerTrainingList(Principal principal,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                    @RequestParam(required = false) String traineeName) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if(userOptional.get().getRole() != Role.TRAINER) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have permission");
        List<TrainingDTO> trainings = trainingService.findByTrainerUsername(username, fromDate, toDate, traineeName);
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }


    @PostMapping("trainer/addTraining")
    public ResponseEntity<?> addTraining(Principal principal, @RequestBody CreateTrainingDTO createTrainingDTO) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if(userOptional.get().getRole() != Role.TRAINER) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have permission");
        Training training = null;
        try {
            training = trainingService.addTraining(createTrainingDTO);
        }catch (Exception e) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); }
        return ResponseEntity.status(HttpStatus.OK).body(TrainingMapper.toDTO(training, false));
    }
}