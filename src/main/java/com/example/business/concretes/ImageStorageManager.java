package com.example.business.concretes;

import com.example.business.abstracts.IImageStorageService;
import com.example.exception.ImageUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageManager implements IImageStorageService {

    private static final String UPLOAD_DIR = "uploads/";//Yüklenen dosyaların kaydedileceği ana dizin

    /**
     * MultipartFile olarak gelen görseli local dizine kaydeder.
     *
     * Dosyanın boş olup olmadığı kontrol edilir
     * uploads/ dizini yoksa oluşturulur
     * Dosya ismi UUID ile benzersiz hale getirilir
     * Dosya diske yazılır
     *
     * @param file Yüklenecek görsel dosyası
     * @return     Kaydedilen dosyanın erişim yolu (URL)
     * @throws ImageUploadException Dosya boşsa veya IO hatası oluşursa
     */
    @Override
    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageUploadException("Resim dosyası boş!");
        }

        try {
            // uploads/ klasörü yoksa oluşturulur
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            // Benzersiz dosya adı oluşturulur
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            // Dosya diske kopyalanır
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // Client’a döndürülecek erişim yolu
            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new ImageUploadException("Görüntü yüklenemedi!!");
        }
    }
}
