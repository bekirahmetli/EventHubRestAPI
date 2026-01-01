package com.example.dto.response;

import com.example.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//Kayıt verisini client'a dönerken kullanılan response DTO'su. Registration entity'sinin dış dünyaya açılan temsilidir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private Long id;
    private LocalDateTime registeredAt;
    private RegistrationStatus status;
    private Long userId;
    private String userName;
    private Long ticketTypeId;
    private String ticketTypeName;
    private Long eventId;
    private String eventTitle;
}
