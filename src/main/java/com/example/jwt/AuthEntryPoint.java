package com.example.jwt;

import com.example.result.Result;
import com.example.result.ResultHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * Yetkisiz erişim durumlarında çalışacak olan
 * özel bir AuthenticationEntryPoint implementasyonudur.
 *
 * Bu sınıf, Spring Security tarafından korunan bir kaynağa
 * kimliği doğrulanmamış bir kullanıcı erişmeye çalıştığında devreye girer.
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Yetkisiz bir istek gerçekleştiğinde tetiklenir.
     * Kullanıcı doğrulanmamışsa HTTP 401 hatası döner.
     *
     * @param request  Gelen HTTP isteği
     * @param response HTTP yanıt nesnesi
     * @param authException Kimlik doğrulama hatası hakkında bilgi içerir
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // JSON response oluştur
        Result result = ResultHelper.unauthorizedError("Yetkisiz erişim. Lütfen giriş yapın.");

        // Response ayarları
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON'ı response'a yaz
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}

