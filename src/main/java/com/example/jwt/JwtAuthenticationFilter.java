package com.example.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Her HTTP isteğinde çalışan JWT Authentication filtresi.
 *
 * Bu filter:
 * - Request header içinden JWT’yi okur
 * - Token geçerliyse kullanıcıyı authenticate eder
 * - Authentication bilgisini SecurityContext’e set eder
 *
 * Not:
 * - OncePerRequestFilter sayesinde her request için sadece 1 kez çalışır
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Her HTTP isteğinde çalışan filtre metodu.
     *
     * İstek akışı:
     * Client → Filter → Controller
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");// HTTP request header’ından Authorization bilgisini al

        // Authorization header yoksa veya "Bearer " ile başlamıyorsa JWT kontrolü yapılmaz ve request akışına devam edilir
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);// "Bearer " prefix’i kaldırılarak sadece JWT alınır
        try {
            // Token'ın geçerliliğini kontrol et (hem expired hem de parse hatası kontrolü)
            if (!jwtService.isTokenValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = jwtService.getUsernameByToken(token);// Token içinden username bilgisi alınır

            // Username varsa ve SecurityContext içinde henüz authentication yoksa
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);// Username üzerinden kullanıcı bilgileri yüklenir

                // Kullanıcı mevcutsa ve token süresi dolmamışsa
                if (userDetails != null) {
                    // Spring Security Authentication nesnesi oluşturulur
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,          // authenticated principal
                                    null,                 // credentials (JWT olduğu için null)
                                    userDetails.getAuthorities() // roller / yetkiler
                            );
                    // Authentication detayları set edilir
                    authentication.setDetails(userDetails);

                    // Authentication SecurityContext’e eklenir Bundan sonra request authenticated kabul edilir
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            System.out.println("Token süresi dolmuştur: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("JWT hatası: " + e.getMessage());
        }
        // Filtre zincirinin devam etmesi sağlanır
        filterChain.doFilter(request, response);
    }
}
