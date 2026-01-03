package com.example.result;

import com.example.dto.response.CursorResponse;
import org.springframework.data.domain.Page;

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

    /**
     * Sayfalı listeleme işlemleri için kullanılan response helper metodu.
     *
     * Page nesnesinden alınan bilgiler ile CursorResponse oluşturur ve
     * standart success response formatında client’a döner.
     *
     * @param pageData Spring Data Page nesnesi
     * @param <T>      Sayfa içinde yer alan veri tipi
     * @return         CursorResponse içeren başarılı ResultData
     */
    public static <T> ResultData<CursorResponse<T>> cursor (Page<T> pageData) {
        CursorResponse<T> cursor = new CursorResponse<>();
        cursor.setItems(pageData.getContent());
        cursor.setPageNumber(pageData.getNumber());
        cursor.setPageSize(pageData.getSize());
        cursor.setTotalElements(pageData.getTotalElements());
        return ResultHelper.success(cursor);
    }


    public static Result ok(){
        return new Result(true,"İşlem başarılı","200");
    }

    public static Result notFoundError(String msg){
        return new Result(false,msg,"404");
    }

    /**
     * Conflict (409) hatası için kullanılan response.
     * Genellikle duplicate kayıt veya çakışma durumlarında kullanılır.
     */
    public static Result conflictError(String msg) {
        return new Result(false, msg, "409");
    }

    /**
     * 400 Bad Request durumları için kullanılan standart hata response’u oluşturur.
     * @param msg Client’a döndürülecek hata mesajı
     * @return   400 hata kodu içeren Result nesnesi
     */
    public static Result badRequestError(String msg) {
        return new Result(false, msg, "400");
    }

    /**
     * 401 Unauthorized hatası için Result oluşturur.
     */
    public static Result unauthorizedError(String msg) {
        return new Result(false, msg, "401");
    }
}
