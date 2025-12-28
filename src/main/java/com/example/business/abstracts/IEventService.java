package com.example.business.abstracts;

import com.example.entities.Event;
import org.springframework.data.domain.Page;

public interface IEventService {
    //Event entity için temel CRUD ve listeleme işlemleri
    Event save(Event event);
    Event get(Long id);
    Event update(Event event);
    Page<Event> cursor(int page, int pageSize);
    boolean delete(Long id);
}
