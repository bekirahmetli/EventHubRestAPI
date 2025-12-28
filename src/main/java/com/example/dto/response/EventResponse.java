package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//Etkinlik verisini client'a dönerken kullanılan response DTO'su. Event entity'sinin dış dünyaya açılan temsilidir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String posterUrl;
    private String location;
    private LocalDateTime date;
    private Long categoryId;
    private String categoryName;
    private Long userId; // Organizer ID
    private String organizerName;
}
