package com.example.dto.request.registration;

import com.example.enums.RegistrationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kayıt oluşturma işlemi için kullanılan request DTO'su. Controller katmanında client'tan gelen veriyi taşır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSaveRequest {

    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long userId;

    @NotNull(message = "Bilet türü ID boş olamaz")
    private Long ticketTypeId;

    private RegistrationStatus status;
}
