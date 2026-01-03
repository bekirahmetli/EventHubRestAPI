package com.example.business.concretes;

import com.example.business.abstracts.IAuthenticationService;
import com.example.dao.UserRepo;
import com.example.dto.request.auth.LoginRequest;
import com.example.dto.request.auth.RefreshTokenRequest;
import com.example.dto.request.auth.RegisterRequest;
import com.example.dto.response.AuthenticationResponse;
import com.example.entities.User;
import com.example.enums.AuthProvider;
import com.example.exception.AlreadyExistsException;
import com.example.exception.NotFoundException;
import com.example.jwt.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Authentication (giriş / kayıt / token) işlemlerini yöneten servis sınıfı.
 *
 * Sorumluluklar:
 * - Kullanıcı kaydı (register)
 * - Kullanıcı girişi (login)
 * - JWT access & refresh token üretimi
 * - Refresh token ile yeni access token oluşturma
 */
@Service
public class AuthManager implements IAuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;

    public AuthManager(UserRepo userRepo,
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

        // LOCAL provider için password kontrolü
        if (request.getAuthProvider() == AuthProvider.LOCAL) {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new IllegalArgumentException("LOCAL provider için şifre zorunludur");
            }
        }

        // User entity oluştur
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // Sadece LOCAL provider için password hash'le
        if (request.getAuthProvider() == AuthProvider.LOCAL) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            // OAuth2 kullanıcıları için password null
            user.setPassword(null);
        }
        user.setAuthProvider(request.getAuthProvider());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        // Kullanıcıyı kaydet
        User savedUser = userRepo.save(user);

        // JWT token üret
        String accessToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        // Refresh token'ı database'e kaydet
        savedUser.setRefreshToken(refreshToken);
        userRepo.save(savedUser);

        // Response oluştur
        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
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
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        // Provider kontrolü
        if (user.getAuthProvider() != request.getAuthProvider()) {
            throw new IllegalArgumentException("Bu email " + user.getAuthProvider() + " ile kayıtlı");
        }

        // LOCAL provider için password kontrolü
        if (request.getAuthProvider() == AuthProvider.LOCAL) {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new IllegalArgumentException("LOCAL provider için şifre gereklidir");
            }

            // Spring Security ile authentication yap (şifre kontrolü burada yapılır)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(), // Spring Security username bekliyor
                            request.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                throw new NotFoundException("Giriş başarısız");
            }
        }
        // OAuth2 kullanıcıları için password kontrolü yapılmaz

        // JWT token üret
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Refresh token'ı database'e kaydet
        user.setRefreshToken(refreshToken);
        userRepo.save(user);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Refresh token ile yeni access token üretir.
     *
     * Kurallar:
     * - Refresh token geçerli olmalıdır
     * - Refresh token database'de mevcut olmalıdır
     * - Yeni access token ve refresh token üretilir
     *
     * @param request Refresh token
     * @return Yeni access token ve refresh token içeren response
     */
    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        // Refresh token ile kullanıcıyı bul
        User user = userRepo.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new com.example.exception.NotFoundException("Geçersiz refresh token"));

        // Refresh token'ın geçerliliğini kontrol et
        if (!jwtService.isTokenValid(request.getRefreshToken())) {
            // Refresh token geçersizse database'den temizle
            user.setRefreshToken(null);
            userRepo.save(user);
            throw new com.example.exception.NotFoundException("Refresh token süresi dolmuş");
        }

        // Yeni access token ve refresh token üret
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Yeni refresh token'ı database'e kaydet
        user.setRefreshToken(newRefreshToken);
        userRepo.save(user);

        // Response oluştur
        return AuthenticationResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Kullanıcının çıkış (logout) işlemini gerçekleştirir.
     *
     * İşleyiş:
     * - Client tarafından gönderilen refresh token ile kullanıcı bulunur
     * - Kullanıcı mevcutsa refresh token database'den temizlenir
     * - Böylece refresh token tekrar kullanılamaz hale gelir
     *
     * @param refreshToken Kullanıcıya ait refresh token
     */
    @Override
    public void logout(String refreshToken) {
        User user = userRepo.findByRefreshToken(refreshToken)
                .orElse(null);

        if (user != null) {
            user.setRefreshToken(null); // Refresh token'ı temizle
            userRepo.save(user);
        }
    }

    /**
     * OAuth2 callback'inde kullanıcı oluşturur veya günceller.
     *
     * OAuth2 akışı:
     * 1. Kullanıcı Google/GitHub'a yönlendirilir
     * 2. Kullanıcı giriş yapar
     * 3. OAuth2 provider kullanıcıyı callback URL'ye yönlendirir
     * 4. Bu metod çağrılır ve JWT token üretilir
     *
     * @param email OAuth2'den gelen email
     * @param name OAuth2'den gelen isim
     * @param provider GOOGLE veya GITHUB
     * @param avatarUrl OAuth2'den gelen avatar URL
     * @return AuthenticationResponse (JWT token içerir)
     */
    public AuthenticationResponse oauth2Login(String email, String name, AuthProvider provider, String avatarUrl) {
        // Kullanıcı var mı kontrol et
        Optional<User> optionalUser = userRepo.findByEmail(email);

        User user;
        if (optionalUser.isPresent()) {
            // Kullanıcı varsa güncelle
            user = optionalUser.get();
            user.setName(name);
            user.setAvatarUrl(avatarUrl);
            // Provider değişikliği olabilir (LOCAL'den GOOGLE'a geçiş)
            if (user.getAuthProvider() != provider) {
                user.setAuthProvider(provider);
                user.setPassword(null); // OAuth2'ye geçişte password'u temizle
            }
        } else {
            // Kullanıcı yoksa oluştur
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAuthProvider(provider);
            user.setAvatarUrl(avatarUrl);
            user.setRole(com.example.enums.Role.USER); // Varsayılan rol
            user.setPassword(null); // OAuth2 kullanıcıları için password yok
            user.setCreatedAt(LocalDateTime.now());
        }

        user = userRepo.save(user);

        // JWT token üret
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Refresh token'ı database'e kaydet
        user.setRefreshToken(refreshToken);
        userRepo.save(user);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
