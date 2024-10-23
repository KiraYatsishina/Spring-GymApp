package com.example.springpr.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupTrainee {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
