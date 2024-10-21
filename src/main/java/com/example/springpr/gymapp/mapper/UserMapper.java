package com.example.springpr.gymapp.mapper;

import com.example.springpr.gymapp.dto.UserDTO;
import com.example.springpr.gymapp.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPassword()
        );
    }
}
