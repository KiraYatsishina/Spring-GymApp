package com.example.springpr.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerDTO {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
}