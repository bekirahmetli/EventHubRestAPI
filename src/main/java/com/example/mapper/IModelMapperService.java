package com.example.mapper;

import org.modelmapper.ModelMapper;

/**
 * Request ve Response DTO’ları için kullanılan ModelMapper
 * konfigürasyonlarını sağlayan servis arayüzü.
 *
 * Amaç:
 * - Request DTO → Entity dönüşümlerini ayırmak
 * - Entity → Response DTO dönüşümlerini ayırmak
 * - Mapping kurallarını merkezi bir noktadan yönetmek
 */
public interface IModelMapperService {
    ModelMapper forRequest();
    ModelMapper forResponse();
}
