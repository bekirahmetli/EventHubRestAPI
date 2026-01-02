package com.example.config;

import com.example.dao.UserRepo;
import com.example.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

/**
 * Uygulama genelinde Security ile ilgili bean tanımlarının yapıldığı konfigürasyon sınıfı.
 *
 * Bu sınıf:
 * - UserDetailsService bean’ini oluşturur
 * - AuthenticationProvider tanımlar
 * - AuthenticationManager sağlar
 * - PasswordEncoder (BCrypt) tanımlar
 */
@Configuration
public class AppConfig {
    private final UserRepo userRepo;

    public AppConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Spring Security’nin kullanıcıyı nasıl bulacağını belirleyen bean.
     *
     * Authentication sırasında:
     * - Spring Security bu metodu çağırır
     * - Username ile kullanıcıyı ister
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> optionalUser = userRepo.findByEmail(username);
                if (optionalUser.isPresent()){
                    return optionalUser.get();
                }
                return optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    /**
     * AuthenticationProvider bean’i.
     *
     * Bu provider:
     * - UserDetailsService ile kullanıcıyı çeker
     * - PasswordEncoder ile şifreyi doğrular
     *
     * Username & password login için kullanılır
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * AuthenticationManager bean’i.
     *
     * Login işlemi sırasında:
     * - AuthenticationManager authenticate() metodunu çağırır
     * - Arkada uygun AuthenticationProvider’ı kullanır
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    /**
     * Şifrelerin hashlenmesi için kullanılan PasswordEncoder.
     *
     * BCrypt:
     * - Salt kullanır
     * - Brute-force saldırılara dayanıklıdır
     * - Spring Security’nin önerdiği encoder’dır
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
