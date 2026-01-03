package com.example.api;

import com.example.business.abstracts.IAuthenticationService;
import com.example.dto.request.auth.LoginRequest;
import com.example.dto.request.auth.RefreshTokenRequest;
import com.example.dto.request.auth.RegisterRequest;
import com.example.dto.response.AuthenticationResponse;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication (giriş/kayıt) endpoint'leri.
 *
 * Endpoint'ler:
 * - POST /register → Kullanıcı kaydı
 * - POST /authenticate → Kullanıcı girişi
 */
@RestController
@RequestMapping("/v1/auth") // SecurityConfig'deki path'lerle uyumlu olmalı
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Yeni kullanıcı kaydı oluşturur.
     * POST /v1/auth/register
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResultHelper.created(response);
    }

    /**
     * Kullanıcı girişi yapar.
     * POST /v1/auth/authenticate
     */
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResultHelper.success(response);
    }

    /**
     * Refresh token ile yeni access token alır.
     * POST /v1/auth/refresh
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthenticationResponse response = authenticationService.refreshToken(request);
        return ResultHelper.success(response);
    }
}
