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
        return null;
    }

    @Override
    public Registration update(Registration registration) {
        return null;
    }

    @Override
    public Page<Registration> cursor(int page, int pageSize) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Registration> getByUser(Long userId) {
        return List.of();
    }

    @Override
    public List<Registration> getByTicketType(Long ticketTypeId) {
        return List.of();
    }
}
