package com.example.exception;

/**
 * İlgili kaydın veritabanında bulunamadığı durumlarda fırlatılan exception.
 *
 * Genellikle:
 * - GET
 * - UPDATE
 * - DELETE
 *
 * işlemlerinde, istenen entity mevcut değilse kullanılır.
 */
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
