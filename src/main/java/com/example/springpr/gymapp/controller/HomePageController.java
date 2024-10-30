package com.example.springpr.gymapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @GetMapping("/welcome")
    @Operation(summary = "Welcome message", description = "Returns a welcome message for the Gymapp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Welcome message retrieved successfully.", content = @Content)
    })
    String welcome(){
        return "Welcome to GymApp";
    }
}
