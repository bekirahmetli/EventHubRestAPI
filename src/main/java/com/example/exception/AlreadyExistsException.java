package com.example.exception;

/**
 * Aynı kaydın sistemde zaten mevcut olduğu durumlarda fırlatılan exception.
 *
 * Genellikle:
 * - POST işlemlerinde
 * - Unique alan ihlallerinde kullanılır.
 */
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
