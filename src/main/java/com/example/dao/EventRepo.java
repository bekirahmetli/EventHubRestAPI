package com.example.dao;

import com.example.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepo extends JpaRepository<Event,Long> {
    List<Event> findByCategoryId(Long categoryId);
    List<Event> findByOrganizerId(Long organizerId);
}
