package com.example.springpr.gymapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @RequestMapping("/welcome")
    String welcome(){
        return "Welcome to Gymapp";
    }
}
