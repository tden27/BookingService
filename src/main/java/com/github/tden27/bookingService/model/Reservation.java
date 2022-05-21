package com.github.tden27.bookingService.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Необходимо выбрать бронируемый ресурс")
    private Resource resource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "Значение должно быть будущим либо настоящим временем")
    @NotNull(message = "Необходимо выбрать дату и время начала")
    private LocalDateTime start;

    @Column(name = "duration")
    @Min(value = 1, message = "Продолжительность брони должна быть больше 0 минут")
    private int duration;
}
