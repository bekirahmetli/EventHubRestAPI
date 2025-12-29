package com.example.mapper;

import com.example.dto.response.EventResponse;
import com.example.dto.response.TicketTypeResponse;
import com.example.entities.Event;
import com.example.entities.TicketType;
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
    /**
     * Event entity’si için özel response mapping’leri sağlayan ModelMapper.
     *
     * Amaç:
     * - Nested entity alanlarını (Category, User) düzleştirerek
     *   EventResponse DTO’suna map etmek
     * - Controller katmanında manuel mapping ihtiyacını ortadan kaldırmak
     *
     * @return Event → EventResponse dönüşümleri için özel yapılandırılmış ModelMapper
     */
    @Override
    public ModelMapper forEventResponse() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.LOOSE);

        // Event -> EventResponse için custom mapping
        mapper.typeMap(Event.class, EventResponse.class)
                .addMappings(m -> {
                    m.map(src -> src.getCategory().getId(), EventResponse::setCategoryId);
                    m.map(src -> src.getCategory().getName(), EventResponse::setCategoryName);
                    m.map(src -> src.getUser().getId(), EventResponse::setUserId);
                    m.map(src -> src.getUser().getName(), EventResponse::setOrganizerName);
                });

        return mapper;
    }

    /**
     * TicketType entity'si için özel response mapping'leri sağlayan ModelMapper.
     *
     * Amaç:
     * - Nested entity alanlarını (Event) düzleştirerek
     *   TicketTypeResponse DTO'suna map etmek
     * - Controller katmanında manuel mapping ihtiyacını ortadan kaldırmak
     *
     * @return TicketType → TicketTypeResponse dönüşümleri için özel yapılandırılmış ModelMapper
     */
    @Override
    public ModelMapper forTicketTypeResponse() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        mapper.typeMap(TicketType.class, TicketTypeResponse.class)
                .addMappings(m -> {
                    m.map(src -> src.getEvent().getId(), TicketTypeResponse::setEventId);
                    m.map(src -> src.getEvent().getTitle(), TicketTypeResponse::setEventTitle);
                });

        return mapper;
    }
}
