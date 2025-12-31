package com.example.business.concretes;

import com.example.business.abstracts.IRegistrationService;
import com.example.entities.Registration;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationManager implements IRegistrationService {
    @Override
    public Registration save(Registration registration) {
        return null;
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
