package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Geçici Security konfigürasyonu.
 *
 * ŞU AN TÜM İSTEKLER İZİN VERİLMİŞ DURUMDA (permitAll).
 *
 * NOT: Tüm entity'ler ve katmanlar tamamlandıktan sonra bu dosya
 * güncellenerek gerçek security kuralları eklenecek.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Tüm isteklere şimdilik izin ver
                );

        return http.build();
    }
}
