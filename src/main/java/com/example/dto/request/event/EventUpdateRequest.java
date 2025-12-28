package com.example.dto.request.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Etkinlik güncelleme işlemi için kullanılan request DTO'su. Güncellenecek etkinlik ID'si ve yeni etkinlik bilgilerini içerir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateRequest {

    @NotNull(message = "ID boş olamaz")
    private Long id;

    @NotBlank(message = "Etkinlik başlığı boş olamaz")
    private String title;

    @NotBlank(message = "Etkinlik açıklaması boş olamaz")
    private String description;

    private String posterUrl;

    @NotBlank(message = "Etkinlik konumu boş olamaz")
    private String location;

    @NotNull(message = "Etkinlik tarihi boş olamaz")
    @Future(message = "Etkinlik tarihi gelecekte olmalıdır")
    private LocalDateTime date;

    @NotNull(message = "Kategori ID boş olamaz")
    private Long categoryId;

    @NotNull(message = "Organizer ID boş olamaz")
    private Long userId; // Organizer ID
}
