package com.example.VirtualBank.repository;

import com.example.VirtualBank.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
//he take name of class of the table which is User and Key type of table
public interface UserRepository extends JpaRepository <User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(UUID userId);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsById(UUID userId);
}
