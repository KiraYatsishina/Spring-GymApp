package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.dto.JwtRequest;
import com.example.springpr.gymapp.dto.JwtResponse;
import com.example.springpr.gymapp.dto.UserDTO;
import com.example.springpr.gymapp.exception.AppError;
import com.example.springpr.gymapp.mapper.UserMapper;
import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.repository.TraineeRepository;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtCore jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    public Optional<UserDTO> signUpTrainee(Trainee trainee) {
        return signUpUser(trainee, Role.TRAINEE, traineeRepository::save);
    }

    public Optional<UserDTO> signUpTrainer(Trainer trainer) {
        return signUpUser(trainer, Role.TRAINER, trainerRepository::save);
    }

    private <T extends User> Optional<UserDTO> signUpUser(T user, Role role, Function<T, T> saveFunction) {
        long count = userRepository.countByFirstNameAndLastName(user.getFirstName(), user.getLastName());
        String username = generateUniqueUsername(user.getFirstName(), user.getLastName(), count);
        String generatedPassword = generatePassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);

        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setActive(true);

        T savedUser = saveFunction.apply(user);

        UserDTO userDTO = UserMapper.toDTO(savedUser);
        userDTO.setPassword(generatedPassword);

        return Optional.of(userDTO);
    }

    private String generateUniqueUsername(String firstName, String lastName, long count) {
        return firstName + "." + lastName + (count > 0 ? count : "");
    }

    private String generatePassword() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            passwordBuilder.append(characters.charAt(index));
        }
        return passwordBuilder.toString();
    }
}
