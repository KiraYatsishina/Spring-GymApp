package com.example.springpr.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDTO {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String specialization;
    private List<ShortTraineeDTO> trainees;
}