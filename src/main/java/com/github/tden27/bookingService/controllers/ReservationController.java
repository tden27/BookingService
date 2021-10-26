package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final BookingService bookingService;

    @Autowired
    public ReservationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/new")
    public String createReservationPage(Reservation reservation, Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("resources", Resource.getResources());
        model.addAttribute("user", reservation.getUser());
        model.addAttribute("start", LocalDateTime.now());
        model.addAttribute("duration", 0);
        return "reservations/new";
    }

//    @PostMapping( "/new")
//    public ResponseEntity<?> create(@RequestParam("resource") String resource,
//                      @RequestParam("user") String user,
//                      @RequestParam("start") String start,
//                      @RequestParam("duration") String duration) {
//        Resource resourceToCreate = Resource.valueOf(resource);
//        LocalDateTime startToCreate = LocalDateTime.parse(start);
//        int durationToCreate = Integer.parseInt(duration);
//        int id = bookingService.create(resourceToCreate, user, startToCreate, durationToCreate);
//        return id > 0 ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
//    }

    @PostMapping( "/new")
    @ResponseBody
    public Reservation create(@RequestParam("resource") String resource,
                                    @RequestParam("user") String user,
                                    @RequestParam("start") String start,
                                    @RequestParam("duration") String duration) {
        Resource resourceToCreate = Resource.valueOf(resource);
        LocalDateTime startToCreate = LocalDateTime.parse(start);
        int durationToCreate = Integer.parseInt(duration);
        int id = 0;
        try {
            id = bookingService.create(resourceToCreate, user, startToCreate, durationToCreate);
        } catch (NotPossibleAddBookingWithThisDateAndTime e) {
            e.printStackTrace();
        }
        return new Reservation(id, resourceToCreate, user, startToCreate, durationToCreate);
    }


    @GetMapping("/{id}")
    public String readById(@PathVariable("id") int id, Model model) {
        model.addAttribute("reservation", bookingService.read(id));
        return "reservations/showById";
    }

    @GetMapping("/{id}/update")
    public String updateReservationPage(Model model, @PathVariable("id") int id) {
        Reservation reservation = bookingService.read(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("resources", Resource.getResources());
        model.addAttribute("user_name", reservation.getUser());
        model.addAttribute("start", reservation.getStart());
        model.addAttribute("duration", reservation.getDuration());
        return "reservations/update";
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateReservation(@RequestParam("resource") String resource,
                                    @RequestParam("user") String user,
                                    @RequestParam("start") String start,
                                    @RequestParam("duration") String duration,
                                    @PathVariable("id") int id) {
        Reservation reservation = new Reservation();
        reservation.setResource(Resource.valueOf(resource));
        reservation.setUser(user);
        reservation.setStart(LocalDateTime.parse(start));
        reservation.setDuration(Integer.parseInt(duration));
        final boolean updated = bookingService.update(reservation, id);
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") int id) {
        final boolean deleted = bookingService.delete(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
