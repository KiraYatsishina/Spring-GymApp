package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @DeleteMapping({"/trainer/delete", "/trainee/delete"})
    private ResponseEntity<?> deleteUser(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        boolean isDeleted = userService.deleteUserByUsername(username);

        if (isDeleted) {
            return ResponseEntity.ok("User profile deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }


    @PatchMapping({"/trainee/changeStatus", "/trainer/changeStatus"})
    private ResponseEntity<?> changeStatus(Principal principal, @RequestParam(required = true) boolean status){
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        boolean isChanged = userService.changeStatusByUsername(username, status);

        if (isChanged) {
            return ResponseEntity.ok("User status changed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
