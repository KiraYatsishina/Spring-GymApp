package com.example.springpr.gymapp.repository;

import com.example.springpr.gymapp.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t inner join User u on t.user.userId = u.userId where t.user.userId = :userId and t.loggedOut = false")
    List<Token> findAllAccessTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
}
