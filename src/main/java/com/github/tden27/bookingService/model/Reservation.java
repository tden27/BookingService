package com.github.tden27.bookingService.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Reservation {
    private int id;                 //  id брони
    private Resource resource;      //  забронированный ресурс
    private User user;              // пользователь забронировавший ресурс
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;    // дата и время начала брони
    private int duration;           // продолжительность брони в минутах

    public Reservation() {
    }

    public Reservation(int id, Resource resource, User user, LocalDateTime start, int duration) {
        this.id = id;
        this.resource = resource;
        this.user = user;
        this.start = start;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public Resource getResource() {
        return resource;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", resource=" + resource +
                ", user=" + user +
                ", start=" + start +
                ", duration=" + duration + " minutes" +
                '}';
    }
}
