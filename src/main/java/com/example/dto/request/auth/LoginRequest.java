package com.example.dto.request.auth;

import com.example.enums.AuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kullanıcı giriş işlemi için kullanılan request DTO'su
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email boş olamaz")
    private String email;

    // Password sadece LOCAL provider için zorunlu.OAuth2 için null olabilir
    private String password;

    @NotNull(message = "Auth provider belirtilmelidir")
    private AuthProvider authProvider; // LOCAL, GOOGLE, GITHUB
}
