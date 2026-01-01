package com.example.dto.request.registration;

import com.example.enums.RegistrationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kayıt güncelleme işlemi için kullanılan request DTO'su. Güncellenecek kayıt ID'si ve yeni kayıt bilgilerini içerir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUpdateRequest {

    @NotNull(message = "ID boş olamaz")
    private Long id;

    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long userId;

    @NotNull(message = "Bilet türü ID boş olamaz")
    private Long ticketTypeId;

    @NotNull(message = "Durum boş olamaz")
    private RegistrationStatus status;
}
