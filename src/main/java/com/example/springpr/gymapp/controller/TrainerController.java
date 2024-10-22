package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.TraineeDTO;
import com.example.springpr.gymapp.dto.TrainerDTO;
import com.example.springpr.gymapp.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    @GetMapping("/myProfile")
    public ResponseEntity<TrainerDTO> getMyProfile(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<TrainerDTO> trainerDTOOptional = trainerService.findByUsername(username);

        return trainerDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
