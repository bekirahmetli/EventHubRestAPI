package com.example.mapper;

import org.modelmapper.ModelMapper;

/**
 * Request, Response ve özel durumlar için kullanılan
 * ModelMapper konfigürasyonlarını sağlayan servis arayüzü.
 *
 * Amaç:
 * - DTO ↔ Entity dönüşümlerini merkezi olarak yönetmek
 * - Farklı kullanım senaryoları için farklı mapping stratejileri sunmak
 */
public interface IModelMapperService {
    ModelMapper forRequest();
    ModelMapper forResponse();
    ModelMapper forEventResponse();    // Event entity’si için özel response mapping’lerinde kullanılan ModelMapper
}
