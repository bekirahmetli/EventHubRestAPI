package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kategori oluşturma işlemi için kullanılan request DTO’su. Controller katmanında client’tan gelen veriyi taşır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySaveRequest {
    @NotBlank(message = "Kategori adı boş olamaz")
    private String name;
}
