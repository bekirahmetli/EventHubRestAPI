package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Login/Register işlemlerinden sonra dönen response DTO'su (JWT token içerir)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token; // JWT token
    private String refreshToken; // Refresh token
    private String tokenType = "Bearer"; // Token tipi
    private Long userId; // Kullanıcı ID'si
    private String email; // Email
    private String role; // Kullanıcı rolü (USER, ORGANIZER, ADMIN)
}
