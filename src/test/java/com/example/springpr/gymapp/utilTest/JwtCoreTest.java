package com.example.springpr.gymapp.utilTest;

import com.example.springpr.gymapp.Util.JwtCore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtCoreTest {

    @InjectMocks
    private JwtCore jwtCore;

    private final String secret = "testSecret";
    private final Duration jwtLifetime = Duration.ofMinutes(5);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtCore, "secret", secret);
        ReflectionTestUtils.setField(jwtCore, "jwtLifetime", jwtLifetime);
    }

    @Test
    void testGenerateToken() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails user = new User("testUser", "password", authorities);

        String token = jwtCore.generateToken(user);

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testUser", claims.getSubject());
        assertEquals(Arrays.asList("ROLE_USER"), claims.get("roles", List.class));
    }

    @Test
    void testGetUsername() {
        UserDetails user = new User("testUser", "password", Arrays.asList());
        String token = jwtCore.generateToken(user);

        assertEquals("testUser", jwtCore.getUsername(token));
    }

    @Test
    void testGetRoles() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails user = new User("testUser", "password", authorities);

        String token = jwtCore.generateToken(user);

        assertEquals(Arrays.asList("ROLE_USER"), jwtCore.getRoles(token));
    }

    @Test
    void testTokenExpiration() {
        UserDetails user = new User("testUser", "password", Arrays.asList());
        ReflectionTestUtils.setField(jwtCore, "jwtLifetime", Duration.ofMillis(1));

        String token = jwtCore.generateToken(user);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            Thread.sleep(10);
            jwtCore.getUsername(token);
        });
    }
}