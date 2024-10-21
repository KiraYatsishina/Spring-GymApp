package com.example.springpr.gymapp.dto;

import com.example.springpr.gymapp.model.Role;
import lombok.Data;

@Data
public class RegistrationUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;
}
