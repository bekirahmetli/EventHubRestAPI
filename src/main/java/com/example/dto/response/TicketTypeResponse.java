package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Bilet türü verisini client'a dönerken kullanılan response DTO'su. TicketType entity'sinin dış dünyaya açılan temsilidir.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer quota;
    private Long eventId;
    private String eventTitle;
}
