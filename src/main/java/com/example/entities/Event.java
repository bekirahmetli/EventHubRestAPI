package com.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ticketTypes", "category", "user"})
@EqualsAndHashCode(exclude = {"ticketTypes", "category", "user"})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<TicketType> ticketTypes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_category_id",referencedColumnName = "category_id",nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_user_id",referencedColumnName = "user_id",nullable = false)
    private User user;
}
