package com.example.dao;

import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByEmail(String email); // Email'in veritabanında mevcut olup olmadığını kontrol eder
    Optional<User> findByEmail(String email); // Email ile kullanıcı bulma
    Optional<User> findByRefreshToken(String refreshToken);// Refresh token ile kullanıcı bulma
}
