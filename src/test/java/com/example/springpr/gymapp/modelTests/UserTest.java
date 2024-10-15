package com.example.springpr.gymapp.modelTests;

import com.example.springpr.gymapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "John", "Doe", 0L, true);
    }

    @Test
    public void testGenerateUsernameIfNull() {
        User userWithNullUsername = new User(3L, "Alex", "Johnson", 0L, true);
        assertEquals("Alex.Johnson", userWithNullUsername.getUsername());
    }

    @Test
    public void testSetUsernameCountPassword() {
        user.setCountAndPassword(1L);
        assertEquals("John.Doe1", user.getUsername());
        assertNotNull(user.getPassword());
    }

    @Test
    public void testEqualsAndHashCode() {
        User anotherUser = new User(1L, "John", "Doe", 0L, true);
        anotherUser.setUsername(user.getUsername());
        anotherUser.setPassword(user.getPassword());
        assertEquals(user, anotherUser);
        assertEquals(user.hashCode(), anotherUser.hashCode());

        User differentUser = new User(2L, "Jane", "Smith", 0L, false);
        assertNotEquals(user, differentUser);
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }
}
