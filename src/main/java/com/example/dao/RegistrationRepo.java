package com.example.dao;

import com.example.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration,Long> {
    List<Registration> findByUserId(Long userId); // Kullanıcıya göre kayıtları getirir
    List<Registration> findByTicketTypeId(Long ticketTypeId); // Bilet türüne göre kayıtları getirir
    List<Registration> findByStatus(com.example.enums.RegistrationStatus status); // Duruma göre kayıtları getirir
}
