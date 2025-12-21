package com.example.dao;

import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByEmail(String email); // Email'in veritabanında mevcut olup olmadığını kontrol eder
}
