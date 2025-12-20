package com.example.business.concretes;

import com.example.business.abstracts.ICategoryService;
import com.example.dao.CategoryRepo;
import com.example.entities.Category;
import com.example.exception.AlreadyExistsException;
import com.example.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryManager implements ICategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryManager(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    // Yeni bir kategori oluşturur ve veritabanına kaydeder
    @Override
    public Category save(Category category) {
        if (this.categoryRepo.existsByName(category.getName())){
            // Duplicate name kontrolü
            throw new AlreadyExistsException("Bu kategori adı zaten mevcut: " + category.getName());
        }
        return this.categoryRepo.save(category);
    }

    // Verilen ID’ye sahip kategoriyi getirir, bulunamazsa exception fırlatır
    @Override
    public Category get(Long id) {
        return this.categoryRepo.findById(id).orElseThrow(() -> new NotFoundException("Veri bulunamadı"));
    }

    // Mevcut bir kategoriyi günceller, önce varlığı kontrol edilir
    @Override
    public Category update(Category category) {
        Category existingCategory = this.get(category.getId());
        if (!existingCategory.getName().equals(category.getName()) && this.categoryRepo.existsByName(category.getName())){
            throw new AlreadyExistsException("Bu kategori adı zaten mevcut: " + category.getName());
        }
        return this.categoryRepo.save(category);
    }

    // Sayfalama (pagination) ile kategori listesini döner
    @Override
    public Page<Category> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.categoryRepo.findAll(pageable);
    }

    // Verilen ID’ye sahip kategoriyi siler
    @Override
    public boolean delete(Long id) {
        Category category = this.get(id);
        this.categoryRepo.delete(category);
        return true;
    }
}
