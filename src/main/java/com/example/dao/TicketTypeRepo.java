package com.example.dao;

import com.example.entities.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketTypeRepo extends JpaRepository<TicketType,Long> {
    List<TicketType> findByEventId(Long eventId);// Belirli bir etkinliğe ait tüm bilet türlerini getirir
}
