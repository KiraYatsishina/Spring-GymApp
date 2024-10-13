package com.example.springpr.gymapp.config;

import com.example.springpr.gymapp.CountHelper;
import com.example.springpr.gymapp.DataLoader;
import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.dao.TrainingDAO;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;

@Configuration
@ComponentScan(basePackages = "com.example.springpr.gymapp")
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public TrainerDAO trainerDAO() {
        logger.info("Creating TrainerDAO bean");
        return new TrainerDAO();
    }

    @Bean
    public TraineeDAO traineeDAO() {
        logger.info("Creating TraineeDAO bean");
        return new TraineeDAO();
    }

    @Bean
    public DataLoader dataLoader() {
        logger.info("Creating DataLoader bean");
        return new DataLoader();
    }

    @Bean
    public TrainingDAO trainingDAO() {
        logger.info("Creating TrainingDAO bean");
        return new TrainingDAO(traineeDAO(), trainerDAO());
    }

    @Bean
    public CountHelper countHelper() {
        logger.info("Creating CountHelper bean");
        return new CountHelper(traineeDAO(), trainerDAO());
    }
}