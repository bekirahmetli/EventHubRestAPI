package com.example.dto.request.auth;

import com.example.enums.AuthProvider;
import com.example.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Kullanıcı kayıt işlemi için kullanılan request DTO'su
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    private String password;// Password sadece LOCAL provider için zorunlu.OAuth2 için null olabilir

    @NotNull(message = "Auth provider belirtilmelidir")
    private AuthProvider authProvider;// LOCAL, GOOGLE, GITHUB

    private String avatarUrl;

    @NotNull(message = "Rol boş olamaz")
    private Role role;
}
