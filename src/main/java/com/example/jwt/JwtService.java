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
                // Token’ın geçerlilik süresinin biteceği zamanı belirtir burada 2 saat sonra token geçersiz olur
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(getKey(), SignatureAlgorithm.HS256)// Token’ın hangi anahtar ve algoritma ile imzalanacağını belirtir
                .compact();// JWT’yi String formatına çevirir (header.payload.signature)
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
    public boolean isTokenExpired(String token){
        // Token içinden expiration tarihi alınır
        Date expiredDate = exportToken(token, Claims::getExpiration);

        // Şu anki zaman expiration tarihinden önceyse token geçerlidir
        return new Date().before(expiredDate);
    }

    // SECRET_KEY bilgisinden imzalama için kullanılacak Key nesnesini üretir.
    public Key getKey(){
        // Base64 formatındaki SECRET_KEY byte dizisine çevrilir
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        // HS256 algoritması için uygun Key nesnesi oluşturulur
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
