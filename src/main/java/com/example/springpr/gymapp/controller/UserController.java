package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/trainee/delete")
    public ResponseEntity<?> deleteTrainee(Principal principal) {
        return deleteUser(principal, Role.TRAINEE);
    }

    @DeleteMapping("/trainer/delete")
    public ResponseEntity<?> deleteTrainer(Principal principal) {
        return deleteUser(principal, Role.TRAINER);
    }

    private ResponseEntity<?> deleteUser(Principal principal, Role role) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        if(userOptional.get().getRole() != role) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have permission");
        boolean isDeleted = userService.deleteUserByUsername(username);

        if (isDeleted) {
            return ResponseEntity.ok("User profile deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
