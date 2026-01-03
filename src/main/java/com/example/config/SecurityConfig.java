package com.example.config;

import com.example.jwt.AuthEntryPoint;
import com.example.jwt.JwtAuthenticationFilter;
import com.example.jwt.OAuth2FailureHandler;
import com.example.jwt.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security yapılandırma sınıfı.
 *
 * Bu sınıf:
 * - Hangi endpoint'lerin açık/korumalı olduğunu belirler
 * - JWT tabanlı güvenliği aktif eder
 * - Session kullanımını kapatır (STATELESS)
 * - CORS ayarlarını merkezi olarak yönetir
 * - JwtAuthenticationFilter'ı security filter chain'e ekler
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Kullanıcı giriş (authentication) URL'si
    public static final String AUTHENTICATE = "/v1/auth/authenticate";
    //Kullanıcı kayıt (register) URL'si
    public static final String REGISTER = "/v1/auth/register";
    public static final String REFRESH = "/v1/auth/refresh";
    // OAuth2 callback endpoint'leri
    public static final String OAUTH2_CALLBACK = "/oauth2/callback/**";
    public static final String OAUTH2_LOGIN = "/oauth2/authorization/**";

    public static final String[] SWAGGER_PATHS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter authenticationFilter;
    private final AuthEntryPoint authEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter authenticationFilter, AuthEntryPoint authEntryPoint, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailureHandler oAuth2FailureHandler) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationFilter = authenticationFilter;
        this.authEntryPoint = authEntryPoint;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }


    /**
     * Spring Security filter zincirinin tanımlandığı bean.
     *
     * Burada:
     * - CSRF kapatılır
     * - Endpoint yetkilendirme kuralları tanımlanır
     * - Session yönetimi ayarlanır
     * - JWT filter eklenir
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())// CORS ayarlarını aktif eder.Aşağıda tanımlanan CorsConfigurationSource bean’i kullanılır.
                .csrf(csrf -> csrf.disable()) //CSRF koruması kapatılır.
                .authorizeHttpRequests(auth -> auth
                        // Public endpoint'ler
                        .requestMatchers(AUTHENTICATE,REGISTER,REFRESH).permitAll()
                        // OAuth2 endpoint'leri
                        .requestMatchers(OAUTH2_CALLBACK, OAUTH2_LOGIN).permitAll()
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .anyRequest().authenticated()// Diğer tüm endpoint'ler authentication gerektirir
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// JWT kullanıyoruz, session yok
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                )
                .authenticationProvider(authenticationProvider)//Authentication işlemlerinde kullanılacak provider
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)// JWT filter'ı ekle
                // OAuth2 yapılandırması
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                );
        return http.build();
    }

    /**
     * CORS yapılandırması
     *
     * Frontend backend arasındaki cross-origin istekleri kontrol eder.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));// İzin verilen origin (frontend adresi)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));// İzin verilen header’lar
        config.setAllowCredentials(true);// Authorization header gönderimine izin verir
        config.setExposedHeaders(List.of("Authorization")); // Frontend'in Authorization header'ını okuyabilmesi için

        // Tüm endpoint’ler için bu CORS ayarlarını uygula
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
