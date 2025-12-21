package com.example.business.concretes;

import com.example.business.abstracts.IUserService;
import com.example.dao.UserRepo;
import com.example.entities.User;
import com.example.exception.AlreadyExistsException;
import com.example.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * User entity’si için iş kurallarını ve CRUD operasyonlarını yöneten servis sınıfı.
 *
 * Sorumluluklar:
 * - Kullanıcı oluşturma, güncelleme, silme ve getirme işlemleri
 * - Email adresi için unique kontrolü
 * - NotFound ve AlreadyExists exception’larının yönetimi
 */
@Service
public class UserManager implements IUserService {
    private final UserRepo userRepo;

    public UserManager(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User save(User user) {
        if (userRepo.existsByEmail(user.getEmail())){
            throw new AlreadyExistsException("Bu email adresi zaten mevcut: " + user.getEmail());
        }

        if (user.getCreatedAt() == null){
            user.setCreatedAt(LocalDateTime.now());
        }

        return this.userRepo.save(user);
    }

    @Override
    public User get(Long id) {
        return this.userRepo.findById(id).orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı. ID: " + id));
    }

    @Override
    public User update(User user) {
        User existingUser = this.get(user.getId());

        // Eğer email değiştiriliyorsa ve yeni email başka bir kullanıcıya aitse hata ver
        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepo.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("Bu email adresi zaten mevcut: " + user.getEmail());
        }
        // createdAt'i koru (güncelleme sırasında değişmemeli)
        user.setCreatedAt(existingUser.getCreatedAt());
        return this.userRepo.save(user);
    }

    @Override
    public Page<User> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.userRepo.findAll(pageable);
    }

    @Override
    public boolean delete(Long id) {
        User user = this.get(id);
        this.userRepo.delete(user);
        return true;
    }
}
