package com.example.business.abstracts;

import com.example.entities.Event;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEventService {
    //Event entity için temel CRUD ve listeleme işlemleri
    Event save(Event event);
    Event get(Long id);
    Event update(Event event);
    Page<Event> cursor(int page, int pageSize);
    boolean delete(Long id);
    List<Event> getByCategory(Long categoryId);// Belirli bir kategoriye ait tüm etkinlikleri getirir.
    List<Event> getByUser(Long userId);// Belirli bir kullanıcıya ait tüm etkinlikleri getirir.
}
