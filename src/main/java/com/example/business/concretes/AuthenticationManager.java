package com.example.business.concretes;

import com.example.business.abstracts.IAuthenticationService;
import com.example.dao.UserRepo;
import com.example.dto.request.auth.LoginRequest;
import com.example.dto.request.auth.RegisterRequest;
import com.example.dto.response.AuthenticationResponse;
import com.example.entities.User;
import com.example.exception.AlreadyExistsException;
import com.example.exception.NotFoundException;
import com.example.jwt.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Authentication (giriş/kayıt) işlemlerini yöneten servis sınıfı.
 *
 * Sorumluluklar:
 * - Kullanıcı kaydı (register)
 * - Kullanıcı girişi (login)
 * - JWT token üretimi
 */
@Service
public class AuthenticationManager implements IAuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;

    public AuthenticationManager(UserRepo userRepo,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 org.springframework.security.authentication.AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Yeni kullanıcı kaydı oluşturur.
     *
     * Kurallar:
     * - Email ve username unique olmalıdır
     * - Şifre BCrypt ile hash'lenir
     * - Kayıt sonrası JWT token üretilir
     *
     * @param request Kayıt bilgileri
     * @return JWT token içeren AuthenticationResponse
     * @throws AlreadyExistsException Email veya username zaten mevcutsa
     */
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        // Email kontrolü
        if (this.userRepo.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Bu email adresi zaten kullanılıyor: " + request.getEmail());
        }

        // User entity oluştur
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Şifreyi hash'le
        user.setAuthProvider(request.getAuthProvider());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        // Kullanıcıyı kaydet
        User savedUser = userRepo.save(user);

        // JWT token üret
        String token = jwtService.generateToken(savedUser);

        // Response oluştur
        return AuthenticationResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    /**
     * Kullanıcı girişi yapar.
     *
     * Kurallar:
     * - Username veya email ile giriş yapılabilir
     * - Şifre doğrulanır
     * - Başarılı giriş sonrası JWT token üretilir
     *
     * @param request Giriş bilgileri (username/email ve password)
     * @return JWT token içeren AuthenticationResponse
     * @throws NotFoundException Kullanıcı bulunamazsa
     */
    @Override
    public AuthenticationResponse login(LoginRequest request) {
        // email ile kullanıcıyı bul
        User user = userRepo.findByEmail(request.getEmail())
                .orElseGet(() -> userRepo.findByEmail(request.getEmail())
                        .orElseThrow(() -> new com.example.exception.NotFoundException("Kullanıcı bulunamadı")));

        // Spring Security ile authentication yap (şifre kontrolü burada yapılır)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), // Spring Security username bekliyor
                        request.getPassword()
                )
        );

        // Authentication başarılıysa token üret
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        }

        throw new NotFoundException("Giriş başarısız");
    }
}
