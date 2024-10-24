package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.dto.UserDTO;
import com.example.springpr.gymapp.mapper.UserMapper;
import com.example.springpr.gymapp.model.Role;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.repository.TraineeRepository;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtCore jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public ResponseEntity<String> createAuthToken(UserDTO authRequest) {
        try {
            logger.info("Attempting authentication for user: {}", authRequest.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        logger.info("Authentication successful for user: {}. Token generated.", authRequest.getUsername());
        return ResponseEntity.ok(token);
    }

    public Optional<UserDTO> signUpTrainee(Trainee trainee) {
        return signUpUser(trainee, Role.ROLE_TRAINEE, traineeRepository::save);
    }

    public Optional<UserDTO> signUpTrainer(Trainer trainer) {
        return signUpUser(trainer, Role.ROLE_TRAINER, trainerRepository::save);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedNewPassword);
                userRepository.save(user);
                logger.info("Password for user {} successfully changed.", username);
                return true;
            } else {
                logger.warn("Old password is incorrect for user {}.", username);
                return false;
            }
        }
        logger.warn("User {} not found.", username);
        return false;
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
