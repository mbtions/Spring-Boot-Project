package com.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmailToken(String emailToken);
}
