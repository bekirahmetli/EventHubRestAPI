package com.example.jwt;

import com.example.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT (JSON Web Token) üretme ve okuma işlemlerini yöneten servis sınıfı.
 *
 * Bu sınıf:
 * - Kullanıcıya token üretir
 * - Token içinden username ve role gibi bilgileri okur
 * - Token süresinin geçerli olup olmadığını kontrol eder
 */
@Component
public class JwtService {

    // JWT imzalama ve doğrulama işlemleri için kullanılan gizli anahtar
    public static final String SECRET_KEY = "oHhulXZTRYwbmmbzFsKwutab74uoFfrjUVRvL7qbUKc=";

    //Kullanıcı bilgilerine göre JWT üretir.
    public String generateToken(UserDetails userDetails){
        // Token içine eklenecek ek bilgiler (custom claims) için Map
        Map<String,Object> claimsMap = new HashMap<>();

        // Eğer UserDetails, bizim User entity’miz ise role bilgisini token içine ekliyoruz
        if (userDetails instanceof User){
            User user = (User) userDetails;

            // Kullanıcının rolü token içine "role" claim’i olarak eklenir
            claimsMap.put("role", user.getRole().name());
        }

        // JWT oluşturma işlemi başlar
        return Jwts.builder()
                .claims(claimsMap)// Token içine eklenen custom claim’ler
                .setSubject(userDetails.getUsername())// Token’ın kime ait olduğunu belirtir genelde username veya email kullanılır
                .setIssuedAt(new Date())// Token’ın oluşturulduğu zamanı belirtir
                // Token’ın geçerlilik süresinin biteceği zamanı belirtir burada 20 dk sonra token geçersiz olur
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 20))
                .signWith(getKey(), SignatureAlgorithm.HS256)// Token’ın hangi anahtar ve algoritma ile imzalanacağını belirtir
                .compact();// JWT’yi String formatına çevirir (header.payload.signature)
    }

    /**
     * Refresh token üretir.
     *
     * Refresh token:
     * - Access token'dan daha uzun süreli (4 saat)
     * - Sadece yeni access token almak için kullanılır
     * - Database'de saklanır
     *
     * @param userDetails Kullanıcı bilgileri
     * @return Refresh token string
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claimsMap = new HashMap<>();

        // User entity'sinden role'ü al
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            claimsMap.put("role", user.getRole().name());
        }

        return Jwts.builder()
                .claims(claimsMap)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 4)) // 4 saat
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token'ın geçerliliğini kontrol eder.
     *
     * @param token Kontrol edilecek token
     * @return Token geçerliyse true, değilse false
     */
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Token içerisinden kullanıcının rol bilgisini okur.
    public String getRoleFromToken(String token) {
        // exportToken metodu kullanılarak role claim’i String olarak alınır
        return exportToken(token, claims ->
                claims.get("role", String.class)
        );
    }

    // Token içerisindeki bilgileri (claims) okuyup istenilen alanı döndürmek için kullanılan generic yardımcı metot.
    public <T> T exportToken(String token, Function<Claims, T> claimsTFunction){

        // Token parse edilir ve imza doğrulaması yapılır
        Claims claims = Jwts
                .parser()
                .setSigningKey(getKey())// Token doğrulaması için kullanılacak imzalama anahtarı
                .build()// Parser nesnesi oluşturulur
                .parseClaimsJws(token)// Token çözülür
                .getBody();// Token’ın payload (claims) kısmı alınır

        // Claims içinden istenilen alan fonksiyon ile döndürülür
        return claimsTFunction.apply(claims);
    }

    // Token içerisinden username bilgisini döndürür.
    public String getUsernameByToken(String token){
        // Token’ın subject alanı username olarak okunur
        return exportToken(token, Claims::getSubject);
    }

    // Token’ın süresinin dolup dolmadığını kontrol eder.
    public boolean isTokenExpired(String token) {
        try {
            Date expiredDate = exportToken(token, Claims::getExpiration);
            return expiredDate.before(new Date()); // Süresi dolmuşsa true
        } catch (Exception e) {
            return true; // Hata varsa token geçersiz say
        }
    }

    // SECRET_KEY bilgisinden imzalama için kullanılacak Key nesnesini üretir.
    public Key getKey(){
        // Base64 formatındaki SECRET_KEY byte dizisine çevrilir
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        // HS256 algoritması için uygun Key nesnesi oluşturulur
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
