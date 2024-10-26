package com.example.springpr.gymapp.controllerTests;

import com.example.springpr.gymapp.controller.UserController;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Principal mockPrincipal;

    private String username = "testUser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockPrincipal.getName()).thenReturn(username);
    }

    @Test
    void testDeleteUser_Success() {
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userService.deleteUserByUsername(username)).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser(mockPrincipal);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User profile deleted successfully.", response.getBody());
        verify(userService, times(1)).deleteUserByUsername(username);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.deleteUser(mockPrincipal);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found.", response.getBody());
        verify(userService, times(0)).deleteUserByUsername(username);
    }

    @Test
    void testDeleteUser_DeleteFailed() {
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userService.deleteUserByUsername(username)).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUser(mockPrincipal);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found.", response.getBody());
        verify(userService, times(1)).deleteUserByUsername(username);
    }

    @Test
    void testChangeStatus_Success() {
        User mockUser = new User();
        mockUser.setUsername(username);
        boolean newStatus = true;
        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userService.changeStatusByUsername(username, newStatus)).thenReturn(true);

        ResponseEntity<?> response = userController.changeStatus(mockPrincipal, newStatus);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User status changed successfully.", response.getBody());
        verify(userService, times(1)).changeStatusByUsername(username, newStatus);
    }

    @Test
    void testChangeStatus_NotFound() {
        boolean newStatus = false;
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.changeStatus(mockPrincipal, newStatus);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found.", response.getBody());
        verify(userService, times(0)).changeStatusByUsername(username, newStatus);
    }

    @Test
    void testChangeStatus_ChangeFailed() {
        User mockUser = new User();
        mockUser.setUsername(username);
        boolean newStatus = true;
        when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userService.changeStatusByUsername(username, newStatus)).thenReturn(false);

        ResponseEntity<?> response = userController.changeStatus(mockPrincipal, newStatus);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found.", response.getBody());
        verify(userService, times(1)).changeStatusByUsername(username, newStatus);
    }
}
