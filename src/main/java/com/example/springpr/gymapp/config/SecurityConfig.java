package com.example.springpr.gymapp.config;

import com.example.springpr.gymapp.Util.JwtRequestFilter;
import com.example.springpr.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/GymApp", // all
            "/GymApp/welcome", // all
            "/GymApp/auth", // авторизация
            "/GymApp/signup/trainee", // регистрация
            "/GymApp/signup/trainer", // регистрация
            "/GymApp/logout", // выход
            "/GymApp/trainee/createProfile",
            "/GymApp/trainee/myProfile",
            "/GymApp/trainee/updateProfile",
            "/GymApp/trainee/updateStatus",
            "/GymApp/trainee/updateTrainersList",
            "/GymApp/trainee/trainingsList",
            "/GymApp/trainee/notAssignTrainersList",
            "/GymApp/trainee/delete",

            "/GymApp/trainer/createProfile",
            "/GymApp/trainer/home",
            "/GymApp/trainer/updateProfile",
            "/GymApp/trainer/updateStatus",
            "/GymApp/trainer/trainingsList",
            "/GymApp/trainer/createTraining",
            "/GymApp/trainer/delete"
    };

    private UserService userService;
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtRequestFilter(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/GymApp", "/GymApp/auth",
                                "/GymApp/signup/trainee", "/GymApp/signup/trainer",
                                "/GymApp/welcome","/GymApp/logout").permitAll()
                        .requestMatchers("/GymApp/trainee/**").hasRole("TRAINEE")
                        .requestMatchers("/GymApp/trainer/**").hasRole("TRAINER")
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}