package com.example.business.abstracts;

import com.example.dto.request.auth.LoginRequest;
import com.example.dto.request.auth.RefreshTokenRequest;
import com.example.dto.request.auth.RegisterRequest;
import com.example.dto.response.AuthenticationResponse;

//Authentication (giriş/kayıt) işlemlerini yöneten servis interface'i
public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request); // Kullanıcı kaydı
    AuthenticationResponse login(LoginRequest request); // Kullanıcı girişi
    AuthenticationResponse refreshToken(RefreshTokenRequest request);// Refresh token kullanarak yeni bir access token üretir.
}