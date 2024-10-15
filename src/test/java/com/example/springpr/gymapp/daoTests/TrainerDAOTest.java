package com.example.springpr.gymapp.daoTests;

import com.example.springpr.gymapp.dao.TrainerDAO;
import com.example.springpr.gymapp.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerDAOTest {

    private TrainerDAO trainerDAO;

    @BeforeEach
    public void setUp() {
        trainerDAO = new TrainerDAO();
    }

    @Test
    void createTrainerTest() {
        Trainer trainer = new Trainer(1L, "Kira", "Yatsishina", 0L,
                true, "Yoga");

        trainerDAO.createTrainer(trainer);
        Trainer retrievedTrainer = trainerDAO.getTrainerById(1L);

        assertNotNull(retrievedTrainer);
        assertEquals(retrievedTrainer.getId(), trainer.getId());
        assertEquals(retrievedTrainer.getFirstName(), trainer.getFirstName());
        assertEquals(retrievedTrainer.getLastName(), trainer.getLastName());
        assertEquals(retrievedTrainer.getUsername(), trainer.getFirstName() + "." + trainer.getLastName());
        assertNotNull(retrievedTrainer.getPassword());
        assertEquals(retrievedTrainer.getIsActive(), trainer.getIsActive());
        assertEquals(retrievedTrainer.getTrainingType(), trainer.getTrainingType());
    }

    @Test
    void createTrainerWithExistingFirstnameLastnameTest() {
        trainerDAO.createTrainer(new Trainer(1L, "John", "Doe", 0L,
                true, "Personal Trainer"));
        trainerDAO.createTrainer(new Trainer(2L, "John", "Doe", 1L,
                false, "Strength Coach"));
        trainerDAO.createTrainer(new Trainer(3L, "John", "Doe", 2L,
                true, "Personal Trainer"));
        trainerDAO.createTrainer(new Trainer(4L, "Kira", "Yatsishina", 0L,
                true, "Yoga Instructor"));

        Trainer trainer = new Trainer(5L, "John", "Doe", 3L,
                true, "Strength Coach");

        trainerDAO.createTrainer(trainer);
        Trainer retrievedTrainer = trainerDAO.getTrainerById(5L);

        assertNotNull(retrievedTrainer);
        assertEquals(retrievedTrainer.getId(), trainer.getId());
        assertEquals(retrievedTrainer.getFirstName(), trainer.getFirstName());
        assertEquals(retrievedTrainer.getLastName(), trainer.getLastName());
        assertEquals(retrievedTrainer.getUsername(), trainer.getFirstName() + "." + trainer.getLastName() + 3);
        assertNotNull(retrievedTrainer.getPassword());
        assertEquals(retrievedTrainer.getIsActive(), trainer.getIsActive());
        assertEquals(retrievedTrainer.getTrainingType(), trainer.getTrainingType());
    }

    @Test
    void getTrainerByIdTest() {
        Trainer trainer =new Trainer(1L, "Kira", "Yatsishina", 0L,
                true, "Yoga");

        trainerDAO.createTrainer(trainer);
        Trainer retrievedTrainer = trainerDAO.getTrainerById(1L);

        assertNotNull(retrievedTrainer);
        assertEquals(retrievedTrainer.getId(), trainer.getId());
        assertEquals(retrievedTrainer.getFirstName(), trainer.getFirstName());
        assertEquals(retrievedTrainer.getLastName(), trainer.getLastName());
        assertEquals(retrievedTrainer.getUsername(), trainer.getFirstName() + "." + trainer.getLastName());
        assertNotNull(retrievedTrainer.getPassword());
        assertEquals(retrievedTrainer.getIsActive(), trainer.getIsActive());
        assertEquals(retrievedTrainer.getTrainingType(), trainer.getTrainingType());
    }

    @Test
    void getNonExistentTrainerByIdTest() {
        Trainer retrievedTrainer = trainerDAO.getTrainerById(999L);
        assertNull(retrievedTrainer);  // message in loggs
    }

    @Test
    void updateTrainerTest() {
        Trainer trainer =new Trainer(1L, "Kira", "Yatsishina", 0L,
                true, "Yoga");
        Trainer updateTrainer =new Trainer(1L, "NEWKira", "NEWYatsishina", 0L,
                false, "NEW Yoga");

        trainerDAO.createTrainer(trainer);
        long count = 0;
        trainerDAO.updateTrainer(1L, updateTrainer, count);

        Trainer retrievedTrainer = trainerDAO.getTrainerById(1L);

        assertNotNull(retrievedTrainer);
        assertEquals(retrievedTrainer.getId(), trainer.getId());
        assertEquals(retrievedTrainer.getFirstName(), updateTrainer.getFirstName());
        assertEquals(retrievedTrainer.getLastName(), updateTrainer.getLastName());
        assertEquals(retrievedTrainer.getUsername(), "NEWKira.NEWYatsishina");
        assertEquals(retrievedTrainer.getPassword(), trainer.getPassword());
        assertEquals(retrievedTrainer.getIsActive(), updateTrainer.getIsActive());
        assertEquals(retrievedTrainer.getTrainingType(), updateTrainer.getTrainingType());
    }

    @Test
    void updateTrainerWithExistingFirstnameLastnameTest() {
        trainerDAO.createTrainer(new Trainer(1L, "John", "Doe", 0L,
                true, "Personal Trainer"));
        trainerDAO.createTrainer(new Trainer(2L, "John", "Doe", 1L,
                true, "Personal Trainer"));
        trainerDAO.createTrainer(new Trainer(3L, "Jane", "Doe", 0L,
                true, "Strength Coach"));

        Trainer updateTrainer = new Trainer(3L, "John", "Doe", 2L,
                false,"Yoga Instructor");

        long count = 2;
        trainerDAO.updateTrainer(3L, updateTrainer, 2);
        Trainer retrievedTrainer = trainerDAO.getTrainerById(3L);

        assertNotNull(retrievedTrainer);
        assertEquals(retrievedTrainer.getId(), updateTrainer.getId());
        assertEquals(retrievedTrainer.getFirstName(), updateTrainer.getFirstName());
        assertEquals(retrievedTrainer.getLastName(), updateTrainer.getLastName());
        assertEquals(retrievedTrainer.getUsername(), "John.Doe2");
        assertNotNull(retrievedTrainer.getPassword());
        assertEquals(retrievedTrainer.getIsActive(), updateTrainer.getIsActive());
        assertEquals(retrievedTrainer.getTrainingType(), updateTrainer.getTrainingType());
    }

    @Test
    void updateNonExistentTrainerTest() {
        Trainer updateTrainer = new Trainer(3L, "John", "Doe", 0L,
                false,"Yoga Instructor");

        trainerDAO.updateTrainer(999L, updateTrainer, 0); // message in loggs
    }

    @Test
    void deleteTrainerTest() {
        Trainer trainer = new Trainer(4L, "John", "Doe", 0L,
                false,"Yoga Instructor");

        trainerDAO.createTrainer(trainer);
        trainerDAO.deleteTrainer(4L);
        Trainer retrievedTrainer = trainerDAO.getTrainerById(4L);

        assertNull(retrievedTrainer);
    }

    @Test
    void deleteNonExistentTrainerTest() {
        trainerDAO.deleteTrainer(999L); // message in loggs
    }

    @Test
    void getAllTrainersTest() {
        List<Trainer> expectedTrainers = Arrays.asList(
                new Trainer(1L, "John", "Doe", 0L,
                        true, "Personal Trainer"),
                new Trainer(2L, "John", "Doe", 1L,
                        true, "Personal Trainer")
        );

        for (Trainer trainer : expectedTrainers) {
            trainerDAO.createTrainer(trainer);
        }

        List<Trainer> allTrainers = trainerDAO.getAllTrainers();
        assertEquals(2, allTrainers.size());
        assertTrue(allTrainers.containsAll(expectedTrainers));
    }
}
