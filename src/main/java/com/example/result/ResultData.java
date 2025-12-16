package com.example.result;

import lombok.Getter;

/**
 * Veri içeren API yanıtları için kullanılan generic result sınıfı.
 * Result sınıfını genişleterek operasyon sonucu ile birlikte
 * response datasını client’a döner.
 */
@Getter
public class ResultData <T> extends Result {
    private T data;

    /**
     * ResultData constructor’ı.
     *
     * @param status  Operasyonun başarılı olup olmadığı
     * @param message Client’a gösterilecek mesaj
     * @param code    Operasyon sonucu kodu
     * @param data    Response olarak döndürülecek veri
     */
    public ResultData(Boolean status, String message, String code, T data) {
        super(status, message, code);
        this.data = data;
    }
}
