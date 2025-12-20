package com.example.exception;

import com.example.result.Result;
import com.example.result.ResultData;
import com.example.result.ResultHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Uygulama genelinde fırlatılan exception’ları yakalayıp
 * standart API response formatında client’a dönen global handler sınıfı.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @Valid anotasyonu ile tetiklenen doğrulama hatalarını yakalar.
     *
     * MethodArgumentNotValidException:
     * - Request DTO üzerindeki constraint ihlallerinde fırlatılır
     * - Tüm field hata mesajları liste halinde client’a döner
     *
     * @param e Validation exception
     * @return 400 BAD REQUEST içeren standart hata response’u
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultData<List<String>>> handleValidationErrors(MethodArgumentNotValidException e){

        List<String>validationErrorList = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ResultData<List<String>>resultData = new ResultData<>(false,"Veri doğrulama hatası","400",validationErrorList);
        return new ResponseEntity<>(resultData, HttpStatus.BAD_REQUEST);

    }

    /**
     * İlgili kaydın bulunamadığı durumlarda fırlatılan NotFoundException’ı yakalar.
     *
     * @param e NotFoundException
     * @return 404 NOT FOUND içeren standart hata response’u
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result> handleNotFoundException(NotFoundException e){
        return new ResponseEntity<>(ResultHelper.notFoundError(e.getMessage()),HttpStatus.NOT_FOUND);
    }
}

