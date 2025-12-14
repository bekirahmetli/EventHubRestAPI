package com.example.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
public class ModelManagerService implements IModelMapperService{
    private final ModelMapper modelMapper;

    public ModelManagerService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Request DTO → Entity dönüşümleri için kullanılan ModelMapper konfigürasyonu.
     *
     * STANDARD matching:
     * - Alan isimlerinin birebir eşleşmesini bekler
     * - Yanlış veya belirsiz mapping riskini azaltır
     */
    @Override
    public ModelMapper forRequest() {
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STANDARD);
        return this.modelMapper;
    }

    /**
     * Entity → Response DTO dönüşümleri için kullanılan ModelMapper konfigürasyonu.
     *
     * LOOSE matching:
     * - Daha esnek alan eşleşmesine izin verir
     * - Response DTO’larda alan sayısı/isimleri farklı olabilir
     */
    @Override
    public ModelMapper forResponse() {
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.LOOSE);
        return this.modelMapper;
    }
}
