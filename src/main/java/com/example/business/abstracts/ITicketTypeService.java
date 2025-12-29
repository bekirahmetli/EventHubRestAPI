package com.example.business.abstracts;

import com.example.entities.TicketType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITicketTypeService {
    //TicketType entity için temel CRUD ve listeleme işlemleri
    TicketType save(TicketType ticketType);
    TicketType get(Long id);
    TicketType update(TicketType ticketType);
    Page<TicketType> cursor(int page,int pageSize);
    boolean delete(Long id);
    List<TicketType> getByEventId(Long eventId);// Etkinliğe göre bilet türlerini getirir
}
