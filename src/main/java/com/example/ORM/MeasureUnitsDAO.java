package com.example.ORM;

import com.example.models.Measure_Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasureUnitsDAO extends JpaRepository<Measure_Unit, Long> {
    List<Measure_Unit> findAll();
}
