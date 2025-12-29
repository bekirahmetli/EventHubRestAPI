package com.example.business.abstracts;

import org.springframework.web.multipart.MultipartFile;

public interface IImageStorageService {
    /**
     * Gönderilen dosyayı ilgili storage ortamına kaydeder.
     *
     * @param file Kaydedilecek MultipartFile
     * @return Kaydedilen dosyanın erişim yolu veya URL bilgisi
     */
    String save(MultipartFile file);
}
