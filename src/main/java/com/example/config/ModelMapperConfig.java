package com.example.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    /**
     * Uygulama genelinde kullanılacak ModelMapper bean’ini tanımlar.
     *
     * Bu bean:
     * - DTO ↔ Entity dönüşümlerinde kullanılır
     * - ModelManagerService üzerinden konfigüre edilerek yönetilir
     */
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
