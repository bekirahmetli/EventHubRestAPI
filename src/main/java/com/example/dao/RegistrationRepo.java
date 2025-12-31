package com.example.dao;

import com.example.entities.Registration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepo {
    List<Registration> findByUserId(Long userId); // Kullanıcıya göre kayıtları getirir
}
