package com.example.ORM;

import com.example.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Long> {
    List<Category> findAll(); // Metodo predefinito per ottenere tutte le categorie
}
