package com.example.business.concretes;

import com.example.business.abstracts.ITicketTypeService;
import com.example.dao.EventRepo;
import com.example.dao.TicketTypeRepo;
import com.example.entities.Event;
import com.example.entities.TicketType;
import com.example.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketTypeManager implements ITicketTypeService {
    private final TicketTypeRepo ticketTypeRepo;
    private final EventRepo eventRepo;

    public TicketTypeManager(TicketTypeRepo ticketTypeRepo, EventRepo eventRepo) {
        this.ticketTypeRepo = ticketTypeRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public TicketType save(TicketType ticketType) {
        Event event = this.eventRepo.findById(ticketType.getEvent().getId())
                .orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı. ID: " + ticketType.getEvent().getId()));
        ticketType.setEvent(event);

        return this.ticketTypeRepo.save(ticketType);
    }

    @Override
    public TicketType get(Long id) {
        return this.ticketTypeRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Bilet türü bulunamadı. ID: " + id));
    }

    @Override
    public TicketType update(TicketType ticketType) {
        TicketType existingTicketType = this.get(ticketType.getId());

        if (ticketType.getEvent() != null && ticketType.getEvent().getId() != null){
            Event event = this.eventRepo.findById(ticketType.getEvent().getId())
                    .orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı. ID: " + ticketType.getEvent().getId()));
            ticketType.setEvent(event);
        }else {
            ticketType.setEvent(existingTicketType.getEvent());
        }

        return this.ticketTypeRepo.save(ticketType);
    }

    @Override
    public Page<TicketType> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.ticketTypeRepo.findAll(pageable);
    }

    @Override
    public boolean delete(Long id) {
        TicketType ticketType = this.get(id);
        this.ticketTypeRepo.delete(ticketType);
        return true;
    }

    /**
     * Belirli bir etkinliğe ait tüm bilet türlerini getirir.
     *
     * @param eventId Etkinlik ID'si
     * @return Etkinliğe ait bilet türleri listesi
     */
    @Override
    public List<TicketType> getByEventId(Long eventId) {
        return this.ticketTypeRepo.findByEventId(eventId);
    }
}
