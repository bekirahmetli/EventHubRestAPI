package com.example.dto.request;

import com.example.enums.AuthProvider;
import com.example.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kullanıcı oluşturma işlemi için kullanılan request DTO'su. Controller katmanında client'tan gelen veriyi taşır.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {
    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotNull(message = "Auth provider boş olamaz")
    private AuthProvider authProvider;

    private String avatarUrl;

    @NotNull(message = "Rol boş olamaz")
    private Role role;
}
