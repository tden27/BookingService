package com.github.tden27.bookingService.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "resource")
    private Resource resource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @Column(name = "duration")
    private int duration;

    public Reservation() {}

    public Reservation(Long id, Resource resource, User user, LocalDateTime start, int duration) {
        this.id = id;
        this.resource = resource;
        this.user = user;
        this.start = start;
        this.duration = duration;
    }

    public Reservation(Resource resource, User user, LocalDateTime start, int duration) {
        this.resource = resource;
        this.user = user;
        this.start = start;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", resource=" + resource +
                ", user=" + user.getName() +
                ", start=" + start +
                ", duration=" + duration +
                '}';
    }
}
