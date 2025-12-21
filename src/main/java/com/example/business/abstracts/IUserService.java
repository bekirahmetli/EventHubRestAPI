package com.example.business.abstracts;

import com.example.entities.User;
import org.springframework.data.domain.Page;

public interface IUserService {
    //User entity için temel CRUD ve listeleme işlemleri
    User save(User user);
    User get(Long id);
    User update(User user);
    Page<User> cursor(int page,int pageSize);
    boolean delete(Long id);
}
