package com.example.business.abstracts;

import com.example.entities.Registration;
import org.springframework.data.domain.Page;

public interface IRegistrationService {
    //Registration entity için temel CRUD (Create, Read, Update, Delete) ve listeleme işlemleri
    Registration save(Registration registration);
    Registration get(Long id);
    Registration update(Registration registration);
    Page<Registration> cursor(int page, int pageSize);
    boolean delete(Long id);
}
