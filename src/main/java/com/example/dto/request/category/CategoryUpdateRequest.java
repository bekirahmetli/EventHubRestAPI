package com.example.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kategori güncelleme işlemi için kullanılan request DTO’su. Güncellenecek kategori ID’si ve yeni kategori adını içerir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {
    @NotNull(message = "ID boş olamaz")
    private Long id;

    @NotBlank(message = "Kategori adı boş olamaz")
    private String name;
}
