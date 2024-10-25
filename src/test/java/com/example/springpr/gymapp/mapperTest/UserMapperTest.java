package com.example.springpr.gymapp.mapperTest;

import com.example.springpr.gymapp.dto.UserDTO;
import com.example.springpr.gymapp.mapper.UserMapper;
import com.example.springpr.gymapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("test.user");
        user.setPassword("securePassword123");
    }

    @Test
    void testToDTO() {
        UserDTO userDTO = UserMapper.toDTO(user);

        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }
}