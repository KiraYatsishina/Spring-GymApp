package com.example.springpr.gymapp.daoTests;

import com.example.springpr.gymapp.dao.TraineeDAO;
import com.example.springpr.gymapp.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTest {

    private TraineeDAO traineeDAO;

    @BeforeEach
    void setUp() {
        traineeDAO = new TraineeDAO();
    }

    @Test
    void createTraineeTest() {
        Trainee trainee = new Trainee(1L, "Kira", "Yatsishina", "Kira.Yatsishina", "myPassword",
                true, LocalDate.of(1988, 3, 3), "Odesa");

        traineeDAO.createTrainee(trainee);
        Trainee retrievedTrainee = traineeDAO.getTraineeById(1L);

        assertNotNull(retrievedTrainee);
        assertEquals(retrievedTrainee.getId(), trainee.getId());
        assertEquals(retrievedTrainee.getFirstName(), trainee.getFirstName());
        assertEquals(retrievedTrainee.getLastName(), trainee.getLastName());
        assertEquals(retrievedTrainee.getUsername(), trainee.getUsername());
        assertEquals(retrievedTrainee.getPassword(), trainee.getPassword());
        assertEquals(retrievedTrainee.getIsActive(), trainee.getIsActive());
        assertEquals(retrievedTrainee.getDateOfBirth(), trainee.getDateOfBirth());
        assertEquals(retrievedTrainee.getAddress(), trainee.getAddress());
    }

    @Test
    void createTraineeWithExistingFirstnameLastnameTest() {
        traineeDAO.createTrainee(new Trainee(1L, "John", "Doe", "John.Doe", "password1",
                true, LocalDate.of(1992, 1, 1), "New York"));
        traineeDAO.createTrainee(new Trainee(2L, "John", "Doe", "John.Doe1", "password2",
                false, LocalDate.of(1962, 6, 1), "New York"));
        traineeDAO.createTrainee(new Trainee(3L, "John", "Doe", "John.Doe2", "password3",
                true, LocalDate.of(1997, 1, 13), "New York"));
        traineeDAO.createTrainee(new Trainee(4L, "Kira", "Yatsishina", "Kira.Yatsishina", "myPassword",
                true, LocalDate.of(1988, 3, 3), "Odesa"));

        Trainee trainee = new Trainee(5L, "John", "Doe", "John.Doe3", null,
                true, LocalDate.of(1988, 3, 3), "Odesa");

        traineeDAO.createTrainee(trainee);
        Trainee retrievedTrainee = traineeDAO.getTraineeById(5L);

        assertNotNull(retrievedTrainee);
        assertEquals(retrievedTrainee.getId(), trainee.getId());
        assertEquals(retrievedTrainee.getFirstName(), trainee.getFirstName());
        assertEquals(retrievedTrainee.getLastName(), trainee.getLastName());
        assertEquals(retrievedTrainee.getUsername(), trainee.getUsername());
        assertEquals(retrievedTrainee.getIsActive(), trainee.getIsActive());
        assertEquals(retrievedTrainee.getDateOfBirth(), trainee.getDateOfBirth());
        assertEquals(retrievedTrainee.getAddress(), trainee.getAddress());
    }

    @Test
    void getTraineeByIdTest() {
        Trainee trainee = new Trainee(2L, "Emily", "Jones", "Emily.Jones", "password789",
                false, LocalDate.of(1990, 5, 5), "Somewhere");

        traineeDAO.createTrainee(trainee);
        Trainee retrievedTrainee = traineeDAO.getTraineeById(2L);

        assertNotNull(retrievedTrainee);
        assertEquals(retrievedTrainee.getId(), trainee.getId());
        assertEquals(retrievedTrainee.getFirstName(), trainee.getFirstName());
        assertEquals(retrievedTrainee.getLastName(), trainee.getLastName());
        assertEquals(retrievedTrainee.getUsername(), trainee.getUsername());
        assertEquals(retrievedTrainee.getPassword(), trainee.getPassword());
        assertEquals(retrievedTrainee.getIsActive(), trainee.getIsActive());
        assertEquals(retrievedTrainee.getDateOfBirth(), trainee.getDateOfBirth());
        assertEquals(retrievedTrainee.getAddress(), trainee.getAddress());
    }

    @Test
    void getNonExistentTraineeByIdTest() {
        Trainee retrievedTrainee = traineeDAO.getTraineeById(999L);
        assertNull(retrievedTrainee);  // message in loggs
    }

    @Test
    void updateTraineeTest() {
        Trainee trainee = new Trainee(1L, "John", "Doe", "John.Doe", "password1",
                true, LocalDate.of(1992, 1, 1), "New York");
        Trainee updateTrainee = new Trainee(1L, "NEWJohn", "NEWDoe", null, "password2",
                        false, LocalDate.of(1962, 6, 1), "Odesa");

        traineeDAO.createTrainee(trainee);
        long count = 0;
        traineeDAO.updateTrainee(1L, updateTrainee, count);

        Trainee retrievedTrainee = traineeDAO.getTraineeById(1L);

        assertNotNull(retrievedTrainee);
        assertEquals(retrievedTrainee.getId(), trainee.getId());
        assertEquals(retrievedTrainee.getFirstName(), updateTrainee.getFirstName());
        assertEquals(retrievedTrainee.getLastName(), updateTrainee.getLastName());
        assertEquals(retrievedTrainee.getUsername(), "NEWJohn.NEWDoe");
        assertEquals(retrievedTrainee.getPassword(), updateTrainee.getPassword());
        assertEquals(retrievedTrainee.getIsActive(), updateTrainee.getIsActive());
        assertEquals(retrievedTrainee.getDateOfBirth(), updateTrainee.getDateOfBirth());
        assertEquals(retrievedTrainee.getAddress(), updateTrainee.getAddress());
    }

    @Test
    void updateTraineeWithExistingFirstnameLastnameTest() {
        traineeDAO.createTrainee(new Trainee(1L, "John", "Doe", "John.Doe", "password123",
                true, LocalDate.of(1992, 1, 1), "New York"));
        traineeDAO.createTrainee( new Trainee(2L, "John", "Doe", "John.Doe1", "password1",
                true, LocalDate.of(1992, 1, 1), "Odesa"));
        traineeDAO.createTrainee(new Trainee(3L, "Jane", "Doe", "Jane.Doe", "password456",
                false, LocalDate.of(1993, 2, 2), "Los Angeles"));

        Trainee updateTrainee = new Trainee(3L, "John", "Doe", null, "password4",
                true, LocalDate.of(1992, 1, 1), "New York");

        long count = 2;
        traineeDAO.updateTrainee(3L, updateTrainee, 2);
        Trainee retrievedTrainee = traineeDAO.getTraineeById(3L);

        assertNotNull(retrievedTrainee);
        assertEquals(retrievedTrainee.getId(), 3L);
        assertEquals(retrievedTrainee.getFirstName(), updateTrainee.getFirstName());
        assertEquals(retrievedTrainee.getLastName(), updateTrainee.getLastName());
        assertEquals(retrievedTrainee.getUsername(), "John.Doe2");
        assertEquals(retrievedTrainee.getPassword(), updateTrainee.getPassword());
        assertEquals(retrievedTrainee.getIsActive(), updateTrainee.getIsActive());
        assertEquals(retrievedTrainee.getDateOfBirth(), updateTrainee.getDateOfBirth());
        assertEquals(retrievedTrainee.getAddress(), updateTrainee.getAddress());

    }

    @Test
    void updateNonExistentTraineeTest() {
        Trainee trainee = new Trainee(999L, "Non", "Existent", "non.existent", "nopass",
                true, LocalDate.of(1995, 4, 4), "nowhere");

        traineeDAO.updateTrainee(999L, trainee, 0); // message in loggs
    }

    @Test
    void deleteTraineeTest() {
        Trainee trainee = new Trainee(4L, "Sarah", "Johnson", "Sarah.Johnson", "password102",
                false, LocalDate.of(1993, 5, 5), "address2");

        traineeDAO.createTrainee(trainee);
        traineeDAO.deleteTrainee(4L);
        Trainee retrievedTrainee = traineeDAO.getTraineeById(4L);

        assertNull(retrievedTrainee);
    }

    @Test
    void deleteNonExistentTraineeTest() {
        traineeDAO.deleteTrainee(999L); // message in loggs
    }

    @Test
    void getAllTraineesTest() {
        List<Trainee> expectedTrainees = Arrays.asList(
                new Trainee(1L, "John", "Doe", "John.Doe", "password123",
                        true, LocalDate.of(1992, 1, 1), "New York"),
                new Trainee(2L, "Jane", "Doe", "Jane.Doe", "password456",
                        false, LocalDate.of(1993, 2, 2), "Los Angeles")
        );

        for (Trainee trainee : expectedTrainees) {
            traineeDAO.createTrainee(trainee);
        }

        List<Trainee> allTrainees = traineeDAO.getAllTrainees();
        assertEquals(2, allTrainees.size());
        assertTrue(allTrainees.containsAll(expectedTrainees));
    }

}
