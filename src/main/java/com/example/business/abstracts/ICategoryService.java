package com.example.business.abstracts;

import com.example.entities.Category;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    //Category entity için temel CRUD (Create, Read, Update, Delete) ve listeleme işlemleri
    Category save(Category category);
    Category get(Long id);
    Category update(Category category);
    Page<Category> cursor(int page,int pageSize);
    boolean delete(Long id);
}
