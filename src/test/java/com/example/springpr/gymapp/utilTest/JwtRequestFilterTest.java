package com.example.springpr.gymapp.utilTest;

import com.example.springpr.gymapp.Util.JwtCore;
import com.example.springpr.gymapp.Util.JwtRequestFilter;
import com.example.springpr.gymapp.model.Token;
import com.example.springpr.gymapp.model.User;
import com.example.springpr.gymapp.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtCore jwtTokenUtils;

    @Mock
    private FilterChain filterChain;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        String jwt = "validJwtToken";
        String username = "testUser";
        List<String> roles = Arrays.asList("ROLE_USER");
        Token token = new Token(1L, "validJwtToken", false, new User());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        when(jwtTokenUtils.getUsername(jwt)).thenReturn(username);
        when(jwtTokenUtils.getRoles(jwt)).thenReturn(roles);
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.of(token));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithExpiredToken() throws ServletException, IOException {
        String jwt = "expiredJwtToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        when(jwtTokenUtils.getUsername(jwt)).thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidSignatureToken() throws ServletException, IOException {
        String jwt = "invalidSignatureToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        when(jwtTokenUtils.getUsername(jwt)).thenThrow(new SignatureException("Invalid signature"));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
    @Test
    void testDoFilterInternalWithLoggedOutToken() throws ServletException, IOException {
        String jwt = "loggedOutJwtToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        Token loggedOutToken = new Token();
        loggedOutToken.setToken(jwt);
        loggedOutToken.setLoggedOut(true);

        when(jwtTokenUtils.getUsername(jwt)).thenReturn("testUser");
        when(tokenRepository.findByToken(jwt)).thenReturn(java.util.Optional.of(loggedOutToken));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithTokenNotInDatabase() throws ServletException, IOException {
        String jwt = "missingTokenInDb";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        when(jwtTokenUtils.getUsername(jwt)).thenReturn("testUser");
        when(tokenRepository.findByToken(jwt)).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            jwtRequestFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("Token not found in database", exception.getMessage());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, never()).doFilter(request, response);
    }
}