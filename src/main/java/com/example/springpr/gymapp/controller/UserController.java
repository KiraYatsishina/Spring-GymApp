package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @DeleteMapping("/trainee/delete")
    public ResponseEntity<?> deleteUser(Principal principal) {
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Request to delete user: {}", transactionId, username);

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            logger.warn("Transaction ID: {}, User {} not found.", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        boolean isDeleted = userService.deleteUserByUsername(username);

        if (isDeleted) {
            logger.info("Transaction ID: {}, User {} deleted successfully.", transactionId, username);
            return ResponseEntity.ok("User profile deleted successfully.");
        } else {
            logger.warn("Transaction ID: {}, User {} not found during deletion.", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }


    @PatchMapping({"/trainee/changeStatus", "/trainer/changeStatus"})
    public ResponseEntity<?> changeStatus(Principal principal, @RequestParam boolean status){
        String transactionId = UUID.randomUUID().toString();
        String username = principal.getName();
        logger.info("Transaction ID: {}, Request to change status of user: {}, New status: {}", transactionId, username, status);

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            logger.warn("Transaction ID: {}, User {} not found.", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        boolean isChanged = userService.changeStatusByUsername(username, status);

        if (isChanged) {
            logger.info("Transaction ID: {}, Status of user {} changed to: {}", transactionId, username, status);
            return ResponseEntity.ok("User status changed successfully.");
        } else {
            logger.warn("Transaction ID: {}, Failed to change status for user {}.", transactionId, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
