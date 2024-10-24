package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.dto.*;
import com.example.springpr.gymapp.mapper.TraineeMapper;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final UserService userService;
    private final TraineeService traineeService;
    private final JwtCore jwtCore;

    @GetMapping("/myProfile")
    public ResponseEntity<TraineeDTO> getMyProfile(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<TraineeDTO> traineeDTOOptional = traineeService.findByUsername(username);

        return traineeDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateTraineeProfile(Principal principal, @RequestBody UpdateTraineeDTO updateTraineeDTO) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();

        if (updateTraineeDTO.getFirstName() == null || updateTraineeDTO.getFirstName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        if (updateTraineeDTO.getLastName() == null || updateTraineeDTO.getLastName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");

        Optional<Trainee> updatedTrainee = traineeService.updateTraineeProfile(username, updateTraineeDTO);
        if (updatedTrainee.isPresent()) {
            UserDetails userDetails = userService.loadUserByUsername(updatedTrainee.get().getUsername());
            String newToken = jwtCore.generateToken(userDetails);
            TraineeDTO traineeDTO = TraineeMapper.toDTO(updatedTrainee.get(), true);
            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("trainee", traineeDTO);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something with request");
    }


    @GetMapping("/notAssignedTrainersList")
    public ResponseEntity<List<ShortTrainerDTO>> getNotAssignedTrainersList(Principal principal) {
        if(principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<TraineeDTO> traineeDTOOptional = traineeService.findByUsername(username);
        if(!traineeDTOOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<ShortTrainerDTO> unassignedTrainers = traineeService.getNotAssignedTrainersList(username);
        return ResponseEntity.ok(unassignedTrainers);
    }
}
