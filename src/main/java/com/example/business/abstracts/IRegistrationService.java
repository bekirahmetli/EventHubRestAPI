package com.example.business.abstracts;

import com.example.entities.Registration;

public interface IRegistrationService {
    //Registration entity için temel CRUD (Create, Read, Update, Delete) ve listeleme işlemleri
    Registration save(Registration registration);
    Registration get(Long id);
}
