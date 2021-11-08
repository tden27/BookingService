package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
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

    @GetMapping("/homePage")
    public String homePage() {
        return "reservations/homePage";
    }

    @GetMapping("/new")
    public String createReservationPage(Reservation reservation, Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("resources", Resource.getResources());
        model.addAttribute("user", reservation.getUser());
        model.addAttribute("start", LocalDateTime.now());
        model.addAttribute("duration", 0);
        if (!model.containsAttribute("error")) {
            model.addAttribute("error");
        }
        return "reservations/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam("resource") String resource,
                         @RequestParam("user") String user,
                         @RequestParam("start") String start,
                         @RequestParam("duration") String duration,
                         Model model) throws NotPossibleAddBookingWithThisDateAndTime {
        Resource resourceToCreate = Resource.valueOf(resource);
        LocalDateTime startToCreate = LocalDateTime.parse(start);
        int durationToCreate = Integer.parseInt(duration);
        int id = bookingService.create(resourceToCreate, user, startToCreate, durationToCreate);
        model.addAttribute("reservation",
                new Reservation(id, resourceToCreate, user, startToCreate, durationToCreate));
        return "reservations/created";
    }

    @GetMapping("/{id}")
    public String readById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("reservation", bookingService.read(id));
        } catch (NotFoundReservationById e) {
            model.addAttribute("reservation", e);
        }
        return "reservations/showById";
    }

    @GetMapping("/searchById")
    public String searchByIdPage(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("id", 0);
        return "reservations/searchById";
    }

    @PostMapping("/searchById")
    public String searchById(@RequestParam("id") String id, Model model) {
        int idReservation = Integer.parseInt(id);
        try {
            model.addAttribute("reservation", bookingService.read(idReservation));
        } catch (NotFoundReservationById e) {
            model.addAttribute("reservation", e.getMessage());
        }
        return "reservations/showById";
    }

    @GetMapping("/{id}/update")
    public String updateReservationPage(Model model, @PathVariable("id") int id) {
        Reservation reservation;
        try {
            reservation = bookingService.read(id);
            model.addAttribute("reservation", reservation);
            model.addAttribute("resources", Resource.getResources());
            model.addAttribute("user_name", reservation.getUser());
            model.addAttribute("start", reservation.getStart());
            model.addAttribute("duration", reservation.getDuration());
        } catch (NotFoundReservationById e) {
            model.addAttribute("reservation", e);
        }
        return "reservations/update";
    }

    @PostMapping("/{id}/update")
    public String updateReservation(@RequestParam("resource") String resource,
                                    @RequestParam("user") String user,
                                    @RequestParam("start") String start,
                                    @RequestParam("duration") String duration,
                                    @PathVariable("id") int id, Model model) {
        Reservation reservation = new Reservation();
        reservation.setResource(Resource.valueOf(resource));
        reservation.setUser(user);
        reservation.setStart(LocalDateTime.parse(start));
        reservation.setDuration(Integer.parseInt(duration));
        try {
            bookingService.update(reservation, id);
            model.addAttribute("reservation", reservation);
        } catch (NotPossibleAddBookingWithThisDateAndTime e) {
            model.addAttribute("reservation", e.getMessage());
        }
        return "reservations/updated";
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") int id) {
        final boolean deleted = bookingService.delete(id);
        return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(NotPossibleAddBookingWithThisDateAndTime.class)
    public String handlerException(NotPossibleAddBookingWithThisDateAndTime e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "redirect:/reservations/new?m=" + e.getMessage();
    }
}
