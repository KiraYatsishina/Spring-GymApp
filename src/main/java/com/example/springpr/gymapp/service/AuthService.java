package com.example.springpr.gymapp.service;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.dto.UserDTO;
import com.example.springpr.gymapp.mapper.UserMapper;
import com.example.springpr.gymapp.model.*;
import com.example.springpr.gymapp.repository.TokenRepository;
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
import java.util.List;
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
    private final LoginAttemptService loginAttemptService;
    private final TokenRepository tokenRepository;

    public ResponseEntity<String> createAuthToken(UserDTO userCredentials) {
        String username = userCredentials.getUsername();
        if(loginAttemptService.isBlocked(username)){
            return ResponseEntity.status(HttpStatus.LOCKED).
                    body("User account is locked due to too many failed login attempts. Try again later.");
        }

        try {
            logger.info("Attempting authentication for user: {}", username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userCredentials.getPassword()));
        } catch (BadCredentialsException e) {
            loginAttemptService.loginFailed(username);
            logger.warn("Failed login attempt for user: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username or password.");
        }
        loginAttemptService.loginSucceeded(username);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String jwt = jwtTokenUtils.generateToken(userDetails);

        Optional<User> user = userRepository.findByUsername(username);
        revokeAllByUser(user);
        saveToken(user.get(), jwt);

        logger.info("Authentication successful for user: {}. Token generated.", username);
        return ResponseEntity.ok(jwt);
    }

    private void revokeAllByUser(Optional<User> user) {
        List<Token> validTokenByUser = tokenRepository.findAllAccessTokensByUser(user.get().getUserId());
        if(!validTokenByUser.isEmpty()){
            validTokenByUser.forEach(token -> token.setLoggedOut(true));
        }
        tokenRepository.saveAll(validTokenByUser);
    }

    private void saveToken(User user, String jwt) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
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
