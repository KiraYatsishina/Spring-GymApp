package com.example.springpr.gymapp.serviceTests;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.dto.UserDTO;
import com.example.springpr.gymapp.model.Trainee;
import com.example.springpr.gymapp.model.Trainer;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.repository.TokenRepository;
import com.example.springpr.gymapp.repository.TraineeRepository;
import com.example.springpr.gymapp.repository.TrainerRepository;
import com.example.springpr.gymapp.repository.UserRepository;
import com.example.springpr.gymapp.service.AuthService;
import com.example.springpr.gymapp.service.LoginAttemptService;
import com.example.springpr.gymapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private JwtCore jwtTokenUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private TokenRepository tokenRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuthTokenSuccess() {
        UserDTO authRequest = new UserDTO();
        authRequest.setUsername("testUser");
        authRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(authRequest.getUsername())).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("jwtToken");
        when(loginAttemptService.isBlocked("testUser")).thenReturn(false);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = authService.createAuthToken(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", response.getBody());
    }

    @Test
    void testCreateAuthTokenFailed() {
        UserDTO authRequest = new UserDTO();
        authRequest.setUsername("testUser");
        authRequest.setPassword("wrongPassword");

        doThrow(new BadCredentialsException("")).when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        ResponseEntity<String> response = authService.createAuthToken(authRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid username or password.", response.getBody());
    }

    @Test
    void testCreateAuthTokenFailedLockedUser(){
        UserDTO authRequest = new UserDTO();
        authRequest.setUsername("testUser");
        authRequest.setPassword("wrongPassword");

        when(loginAttemptService.isBlocked("testUser")).thenReturn(true);

        ResponseEntity<String> response = authService.createAuthToken(authRequest);

        assertEquals(HttpStatus.LOCKED, response.getStatusCode());
        assertEquals("User account is locked due to too many failed login attempts. Try again later.",
                response.getBody());
        verify(loginAttemptService, times(1)).isBlocked("testUser");
        verify(userRepository, never()).findByUsername(anyString());
        verify(tokenRepository, never()).save(any());
    }
    @Test
    void testSignUpTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(userRepository.countByFirstNameAndLastName(trainee.getFirstName(), trainee.getLastName())).thenReturn(0L);
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Optional<UserDTO> result = authService.signUpTrainee(trainee);

        assertTrue(result.isPresent());
        assertEquals("John.Doe", result.get().getUsername());
        assertNotNull(result.get().getPassword());
    }

    @Test
    void testSignUpTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");

        when(userRepository.countByFirstNameAndLastName(trainer.getFirstName(), trainer.getLastName())).thenReturn(1L);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Optional<UserDTO> result = authService.signUpTrainer(trainer);

        assertTrue(result.isPresent());
        assertEquals("Jane.Smith1", result.get().getUsername());
        assertNotNull(result.get().getPassword());
    }

    @Test
    void testChangePasswordSuccess() {
        String username = "testUser";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedOldPass");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPass");

        boolean result = authService.changePassword(username, oldPassword, newPassword);

        assertTrue(result);
        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePasswordIncorrectOldPassword() {
        String username = "testUser";
        String oldPassword = "incorrectOldPass";
        String newPassword = "newPass";

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedOldPass");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        boolean result = authService.changePassword(username, oldPassword, newPassword);

        assertFalse(result);
        verify(userRepository, never()).save(user);
    }

    @Test
    void testChangePasswordUserNotFound() {
        String username = "nonexistentUser";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = authService.changePassword(username, oldPassword, newPassword);

        assertFalse(result);
    }
}

