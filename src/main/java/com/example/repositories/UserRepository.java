package com.example.repositories;

import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Metodi personalizzati per la ricerca, se necessari
    User findByUsername(String username);
    List<User> findByType(String type);
}
