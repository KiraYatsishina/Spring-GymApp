package com.example.springpr.gymapp.dto.Trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerDTO {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
}