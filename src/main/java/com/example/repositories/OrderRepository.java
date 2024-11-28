package com.example.repositories;

import com.example.models.Order;
import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Trova gli ordini per un dato utente

    List<Order> findByUser(User user);

    List<Order> findByUserAndStatus(User user, String status);

    // Puoi aggiungere altre query personalizzate se necessario
}
