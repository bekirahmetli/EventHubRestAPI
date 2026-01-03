package com.example.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

/**
 * OAuth2 başarısız giriş işlemlerini yöneten handler.
 *
 * OAuth2 girişi başarısız olduğunda (kullanıcı iptal etti, hata oluştu vb.)
 * frontend'e hata mesajı ile yönlendirilir.
 */
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // Frontend'e hata mesajı ile yönlendir
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/error")
                .queryParam("error", exception.getMessage())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
