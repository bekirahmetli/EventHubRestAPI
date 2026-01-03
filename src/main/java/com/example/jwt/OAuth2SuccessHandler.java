package com.example.jwt;


import com.example.business.abstracts.IAuthenticationService;
import com.example.dto.response.AuthenticationResponse;
import com.example.enums.AuthProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

/**
 * OAuth2 başarılı giriş işlemlerini yöneten handler.
 *
 * OAuth2 akışı:
 *  Kullanıcı Google/GitHub'a yönlendirilir
 *  Kullanıcı giriş yapar
 *  OAuth2 provider kullanıcıyı callback URL'ye yönlendirir
 *  Bu handler çalışır ve JWT token üretilir
 *  Frontend'e token ile birlikte yönlendirilir
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final IAuthenticationService authenticationService;

    public OAuth2SuccessHandler(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // OAuth2 authentication token'ı al
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2Token.getPrincipal();

        // Provider bilgisini al (google, github, facebook)
        String registrationId = oauth2Token.getAuthorizedClientRegistrationId();
        AuthProvider provider = mapRegistrationIdToAuthProvider(registrationId);

        // OAuth2'den gelen kullanıcı bilgilerini al
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String avatarUrl = oauth2User.getAttribute("picture"); // Google için
        if (avatarUrl == null) {
            avatarUrl = oauth2User.getAttribute("avatar_url"); // GitHub için
        }

        // Kullanıcı oluştur/güncelle ve JWT token üret
        AuthenticationResponse authResponse = authenticationService.oauth2Login(
                email,
                name != null ? name : email,
                provider,
                avatarUrl
        );

        // Frontend'e yönlendir (token query parameter olarak)
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/callback")
                .queryParam("token", authResponse.getToken())
                .queryParam("refreshToken", authResponse.getRefreshToken())
                .queryParam("userId", authResponse.getUserId())
                .queryParam("email", authResponse.getEmail())
                .queryParam("role", authResponse.getRole())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    // OAuth2 registration ID'sini AuthProvider enum'ına çevirir.
    private AuthProvider mapRegistrationIdToAuthProvider(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProvider.GOOGLE;
            case "github" -> AuthProvider.GITHUB;
            default -> throw new IllegalArgumentException("Bilinmeyen OAuth2 provider: " + registrationId);
        };
    }
}
