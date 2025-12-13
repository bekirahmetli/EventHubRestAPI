package com.example.business.concretes;

import com.example.business.abstracts.ICategoryService;
import com.example.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CategoryManager implements ICategoryService {
    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public Category get(Long id) {
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public Page<Category> cursor(int page, int pageSize) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
