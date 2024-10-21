package com.example.springpr.gymapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "TRAINEES")
@PrimaryKeyJoinColumn(name = "user_id")
public class Trainee extends User {

    @Column(name = "trainee_id")
    private Long traineeId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id", referencedColumnName = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id", referencedColumnName = "trainer_id")
    )
    private List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY)
    private List<Training> trainings = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer()
                .getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Trainee trainee = (Trainee) o;
        return getId() != null && Objects.equals(getId(), trainee.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode()
                : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", isActive=" + isActive() +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}
