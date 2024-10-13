package com.example.springpr.gymapp.model;

import java.util.Objects;

public class Trainer extends User{

    private TrainingType trainingType;

    public Trainer(Long id, String firstName, String lastName, String username, String password, boolean isActive, String trainingType) {
        super(id, firstName, lastName, username, password, isActive);
        this.trainingType = new TrainingType(trainingType);
    }

    public Trainer() {}

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", isActive=" + getIsActive() +
                ", trainingType=" + trainingType +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), trainingType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Trainer trainer = (Trainer) o;
        return Objects.equals(trainingType, trainer.trainingType);
    }
}
