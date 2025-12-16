package com.example.result;

import lombok.Getter;

/**
 * API yanıtlarının genel durumunu, mesajını ve kodunu tutan DTO.
 * Operasyon sonuçlarını ve ilgili mesajları client'a döner.
 */
@Getter
public class Result {
    private Boolean status;
    private String message;
    private String code;

    public Result(Boolean status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
