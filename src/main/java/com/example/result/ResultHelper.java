package com.example.result;

/**
 * API response’ları için hazır Result ve ResultData nesneleri
 * oluşturmaya yardımcı olan helper sınıfı.
 *
 * Amaç:
 * - Controller katmanında tekrar eden response oluşturma kodlarını azaltmak
 * - Standart ve tutarlı API cevapları üretmek
 */
public class ResultHelper {
    /**
     * Başarılı bir POST işlemi sonrası kullanılan response.
     *
     * @param data Oluşturulan ve client’a döndürülecek veri
     * @param <T>  Döndürülecek veri tipi
     * @return     201 status kodu ile oluşturulmuş ResultData
     */
    public static <T> ResultData <T> created(T data){
        return new ResultData<>(true,"Kayıt eklendi","201",data);
    }

    /**
     * Başarılı bir işlem sonrası kullanılan response.
     *
     * @param data Client’a döndürülecek veri
     * @param <T>  Döndürülecek veri tipi
     * @return     200 status kodu ile oluşturulmuş ResultData
     */
    public static <T> ResultData <T> success(T data){
        return new ResultData<>(true,"İşlem başarılı","200",data);
    }
}
