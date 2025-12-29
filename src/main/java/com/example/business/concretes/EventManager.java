package com.example.business.concretes;

import com.example.business.abstracts.IEventService;
import com.example.dao.CategoryRepo;
import com.example.dao.EventRepo;
import com.example.dao.UserRepo;
import com.example.entities.Category;
import com.example.entities.Event;
import com.example.entities.User;
import com.example.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Event entity’si için iş kurallarını ve CRUD operasyonlarını yöneten servis sınıfı.
 *
 * Sorumluluklar:
 * - Event oluşturma, güncelleme, silme ve getirme işlemleri
 * - Event ↔ Category ve Event ↔ User (organizer) ilişkilerinin doğrulanması
 * - İlgili entity’ler bulunamazsa NotFoundException fırlatılması
 */
@Service
public class EventManager implements IEventService {
    private final EventRepo eventRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    public EventManager(EventRepo eventRepo, CategoryRepo categoryRepo, UserRepo userRepo) {
        this.eventRepo = eventRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    /**
     * Yeni bir etkinlik (Event) oluşturur.
     *
     * Kurallar:
     * - Event’e ait kategori mevcut olmalıdır
     * - Event’i oluşturan kullanıcı mevcut olmalıdır
     *
     * @param event Kaydedilecek event entity’si
     * @return Kaydedilen event
     * @throws NotFoundException Kategori veya kullanıcı bulunamazsa
     */
    @Override
    public Event save(Event event) {
        Category category = this.categoryRepo.findById(event.getCategory().getId())
                .orElseThrow(() -> new NotFoundException("Kategori bulunamadı. ID: " + event.getCategory().getId()));

        User user = this.userRepo.findById(event.getUser().getId())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı. ID: " + event.getUser().getId()));

        event.setCategory(category);
        event.setUser(user);

        return this.eventRepo.save(event);
    }

    @Override
    public Event get(Long id) {
        return this.eventRepo.findById(id).orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı. ID: " + id));
    }

    /**
     * Mevcut bir etkinliği günceller.
     *
     * Kurallar:
     * - Event mevcut olmalıdır
     * - Kategori veya kullanıcı gönderilmişse doğrulanır
     * - Gönderilmeyen ilişkiler eski değerleriyle korunur
     *
     * @param event Güncellenecek event entity’si
     * @return Güncellenmiş event
     * @throws NotFoundException İlgili kategori veya kullanıcı bulunamazsa
     */
    @Override
    public Event update(Event event) {
        Event existingEvent = this.get(event.getId());
        // Kategori güncellemesi
        if (event.getCategory() != null && event.getCategory().getId() != null){
            Category category = this.categoryRepo.findById(event.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("Kategori bulunamadı. ID: " + event.getCategory().getId()));
            event.setCategory(category);
        }else{
            event.setCategory(existingEvent.getCategory());// Kategori gönderilmemişse mevcut değer korunur
        }

        // Organizer (User) güncellemesi
        if(event.getUser() != null && event.getUser().getId() != null){
            User user = this.userRepo.findById(event.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("Kullanıcı (organizer) bulunamadı. ID: " + event.getUser().getId()));
            event.setUser(user);
        }else{
            event.setUser(existingEvent.getUser());// User gönderilmemişse mevcut değer korunur
        }

        return this.eventRepo.save(event);
    }

    @Override
    public Page<Event> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.eventRepo.findAll(pageable);
    }

    @Override
    public boolean delete(Long id) {
        Event event = this.get(id);
        this.eventRepo.delete(event);
        return true;
    }

    //Belirli bir kategoriye ait tüm etkinlikleri getirir.
    @Override
    public List<Event> getByCategory(Long categoryId) {
        return this.eventRepo.findByCategoryId(categoryId);
    }

    //Belirli bir kullanıcıya ait tüm etkinlikleri getirir.
    @Override
    public List<Event> getByUser(Long userId) {
        return this.eventRepo.findByUserId(userId);
    }
}
