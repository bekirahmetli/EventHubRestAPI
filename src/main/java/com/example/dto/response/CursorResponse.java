package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Sayfalama (cursor / pagination) sonuçlarını client’a dönerken
 * kullanılan generic response DTO’su.
 *
 * @param <T> Liste içinde dönecek veri tipi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorResponse <T>{
    private int pageNumber;   // Mevcut sayfa numarası
    private int pageSize;    // Sayfa başına düşen kayıt sayısı
    private long totalElements;   // Toplam kayıt sayısı
    private List<T> items;   // İlgili sayfaya ait veri listesi
}
