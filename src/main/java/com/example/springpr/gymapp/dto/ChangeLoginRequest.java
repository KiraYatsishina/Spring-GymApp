package com.example.springpr.gymapp.dto;

import lombok.Data;

@Data
public class ChangeLoginRequest {
    private String oldPassword;
    private String newPassword;
}
