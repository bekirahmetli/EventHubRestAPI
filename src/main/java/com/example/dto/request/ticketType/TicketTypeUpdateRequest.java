package com.example.dto.request.ticketType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Bilet türü güncelleme işlemi için kullanılan request DTO'su. Güncellenecek bilet türü ID'si ve yeni bilet türü bilgilerini içerir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeUpdateRequest {

    @NotNull(message = "ID boş olamaz")
    private Long id;

    @NotBlank(message = "Bilet türü adı boş olamaz")
    private String name;

    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat pozitif bir değer olmalıdır")
    private Double price;

    @NotNull(message = "Kota boş olamaz")
    @Min(value = 1, message = "Kota en az 1 olmalıdır")
    private Integer quota;

    @NotNull(message = "Etkinlik ID boş olamaz")
    private Long eventId;
}
