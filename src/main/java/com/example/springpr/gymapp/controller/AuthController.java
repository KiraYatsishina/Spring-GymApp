package com.example.springpr.gymapp.controller;

import com.example.springpr.gymapp.dto.*;
import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.AuthService;
import com.example.springpr.gymapp.service.TraineeService;
import com.example.springpr.gymapp.service.TrainerService;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController{

    private final AuthService authService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody UserDTO authRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.createAuthToken(authRequest));
    }

    @PostMapping("/signup/trainee") // регистрация
    ResponseEntity<?> signup(@RequestBody SignupTrainee traineeDTO) {
        if (traineeDTO.getFirstName() == null || traineeDTO.getFirstName().length() < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name length must be at least 1 character");
        }
        if (traineeDTO.getLastName() == null || traineeDTO.getLastName().length() < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name length must be at least 1 character");
        }
        Trainee trainee = traineeService.mapToEntity(traineeDTO);
        Optional<UserDTO> createdUser = authService.signUpTrainee(trainee);
        if(createdUser.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    @PostMapping("/signup/trainer") // регистрация
    ResponseEntity<?> signup(@RequestBody SignupTrainer trainerDTO) {
        if (trainerDTO.getFirstName() == null || trainerDTO.getFirstName().length() < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name length must be at least 1 character");
        }
        if (trainerDTO.getLastName() == null || trainerDTO.getLastName().length() < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last name length must be at least 1 character");
        }
        Trainer trainer = trainerService.mapToEntity(trainerDTO);
        Optional<UserDTO> createdUser = authService.signUpTrainer(trainer);
        if(createdUser.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
    }

    @PostMapping("/trainee/changeLogin")
    ResponseEntity<?> changeLoginTrainee(Principal principal,
                                         @RequestBody ChangeLoginRequest request) {
        return changeLogin(principal, request.getOldPassword(), request.getNewPassword(), Role.TRAINEE);
    }

    @PostMapping("/trainer/changeLogin")
    ResponseEntity<?> changeLoginTrainer(Principal principal,
                                         @RequestBody ChangeLoginRequest request) {
        return changeLogin(principal, request.getOldPassword(), request.getNewPassword(), Role.TRAINER);
    }

    private ResponseEntity<?> changeLogin(Principal principal, String oldPassword, String newPassword, Role role) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (!userOptional.isPresent())  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something went wrong");
        if (userOptional.get().getRole() != role)  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have permission");
        boolean changed = authService.changePassword(username, oldPassword, newPassword);
        if (changed) return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

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

//    @PutMapping("/trainee/update")
//    public ResponseEntity<?> updateTrainee(Principal principal, @RequestBody ShortTraineeDTO traineeDTO) {
//
//    }
//
//    @PutMapping("/trainer/update")
//    public ResponseEntity<?> updateTrainer(Principal principal, @RequestBody ShortTrainerDTO trainerDTO) {
//        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        String username = principal.getName();
//        String newToken = updateUser(username);
//        if(newToken == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something with parameters");
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("token", newToken);
//        response.put("trainee", trainerDTO);
//
//        return ResponseEntity.ok(response);
//    }
//
//    private String updateUser(String username, String newFirstname, String newLastname){
//        Optional<User> user = userService.findByUsername(username);
//        if(!user.isPresent()) return null;
//        User userDTO = user.get();
//        if (userDTO.getFirstName() == null || userDTO.getFirstName().isEmpty()
//            ||userDTO.getLastName() == null || userDTO.getLastName().isEmpty()) return null;
//
//
//        Optional<User> updatedUser = userService.updateUserProfile(username, userDTO);
//        if (updatedUser.isPresent()) {
//            UserDetails userDetails = userService.loadUserByUsername(updatedUser.get().getUsername());
//            String newToken = jwtCore.generateToken(userDetails);
//            TrainerDTO trainerDTO = TraineeMapper.toDTO(updatedTrainer.get(), true);
//
//
//        }
//
//    }
}
