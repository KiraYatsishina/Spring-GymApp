package com.example.springpr.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortTrainerDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}