package com.example.business.concretes;

import com.example.business.abstracts.IRegistrationService;
import com.example.dao.RegistrationRepo;
import com.example.dao.TicketTypeRepo;
import com.example.dao.UserRepo;
import com.example.entities.Registration;
import com.example.entities.TicketType;
import com.example.entities.User;
import com.example.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationManager implements IRegistrationService {
    private final RegistrationRepo registrationRepo;
    private final UserRepo userRepo;
    private final TicketTypeRepo ticketTypeRepo;

    public RegistrationManager(RegistrationRepo registrationRepo, UserRepo userRepo, TicketTypeRepo ticketTypeRepo) {
        this.registrationRepo = registrationRepo;
        this.userRepo = userRepo;
        this.ticketTypeRepo = ticketTypeRepo;
    }


    /**
     * Yeni bir Registration kaydı oluşturur.
     * - User ve TicketType varlığı doğrulanır
     * - registeredAt alanı null ise otomatik atanır
     * - status alanı null ise ACTIVE olarak set edilir
     *
     * @param registration Kaydedilecek Registration entity
     * @return Kaydedilen Registration
     */
    @Override
    public Registration save(Registration registration) {
        // User var mı kontrol et
        User user = userRepo.findById(registration.getUser().getId())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı. ID: " + registration.getUser().getId()));

        // TicketType var mı kontrol et
        TicketType ticketType = ticketTypeRepo.findById(registration.getTicketType().getId())
                .orElseThrow(() -> new NotFoundException("Bilet türü bulunamadı. ID: " + registration.getTicketType().getId()));

        registration.setUser(user);
        registration.setTicketType(ticketType);

        // registeredAt otomatik set et (eğer null ise)
        if (registration.getRegisteredAt() == null) {
            registration.setRegisteredAt(LocalDateTime.now());
        }

        // status varsayılan olarak ACTIVE set et (eğer null ise)
        if (registration.getStatus() == null) {
            registration.setStatus(com.example.enums.RegistrationStatus.ACTIVE);
        }

        return this.registrationRepo.save(registration);
    }

    @Override
    public Registration get(Long id) {
        return this.registrationRepo.findById(id).orElseThrow(() -> new NotFoundException("Kayıt bulunamadı. ID: " + id));
    }

    /**
     * Var olan Registration kaydını günceller.
     * - User veya TicketType gönderilmezse mevcut değerler korunur
     * - registeredAt alanı değiştirilemez
     *
     * @param registration Güncellenecek Registration entity
     * @return Güncellenmiş Registration
     */
    @Override
    public Registration update(Registration registration) {
        Registration existingRegistration = this.get(registration.getId());

        // User güncellemesi
        if (registration.getUser() != null && registration.getUser().getId() != null) {
            User user = userRepo.findById(registration.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı. ID: " + registration.getUser().getId()));
            registration.setUser(user);
        } else {
            registration.setUser(existingRegistration.getUser()); // User gönderilmemişse mevcut değer korunur
        }

        // TicketType güncellemesi
        if (registration.getTicketType() != null && registration.getTicketType().getId() != null) {
            TicketType ticketType = ticketTypeRepo.findById(registration.getTicketType().getId())
                    .orElseThrow(() -> new NotFoundException("Bilet türü bulunamadı. ID: " + registration.getTicketType().getId()));
            registration.setTicketType(ticketType);
        } else {
            registration.setTicketType(existingRegistration.getTicketType()); // TicketType gönderilmemişse mevcut değer korunur
        }

        // registeredAt korunur
        registration.setRegisteredAt(existingRegistration.getRegisteredAt());

        return this.registrationRepo.save(registration);
    }

    @Override
    public Page<Registration> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.registrationRepo.findAll(pageable);
    }

    @Override
    public boolean delete(Long id) {
        Registration registration = this.get(id);
        this.registrationRepo.delete(registration);
        return true;
    }

    //Belirli bir kullanıcıya ait Registration kayıtlarını getirir.
    @Override
    public List<Registration> getByUser(Long userId) {
        return this.registrationRepo.findByUserId(userId);
    }

    // Belirli bir TicketType’a ait Registration kayıtlarını getirir.
    @Override
    public List<Registration> getByTicketType(Long ticketTypeId) {
        return this.registrationRepo.findByTicketTypeId(ticketTypeId);
    }
}
