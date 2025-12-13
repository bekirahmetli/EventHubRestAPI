package com.example.dao;

import com.example.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    boolean existsByName(String name); // Verilen kategori adının veritabanında mevcut olup olmadığını kontrol eder
}
