package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("EventhubRestAPI")
                        .version("1.0.0")
                        .description("Eventhub REST API, kullanıcıların etkinlik oluşturabildiği, bilet türlerini yönetebildiği ve etkinlik kayıt süreçlerini takip edebildiği modern bir RESTful servis altyapısıdır.\n" +
                                "\n" +
                                "API; kullanıcı yönetimi, etkinlik ve kategori yönetimi, bilet türleri, kayıt (registration) işlemleri ve JWT tabanlı kimlik doğrulama mekanizmalarını kapsar.\n" +
                                "\n" +
                                "Tüm endpoint’ler REST prensiplerine uygun olarak tasarlanmış olup, Swagger UI üzerinden test edilebilir."))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Token değerini girin:")
                        )
                );
    }
}
