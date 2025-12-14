package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kategori verisini client’a dönerken kullanılan response DTO’su. Category entity’sinin dış dünyaya açılan temsilidir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
}
