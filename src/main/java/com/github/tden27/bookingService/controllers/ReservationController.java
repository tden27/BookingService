package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        model.addAttribute("user", "");
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
        User userToCreate = new User(user);
        LocalDateTime startToCreate = LocalDateTime.parse(start);
        int durationToCreate = Integer.parseInt(duration);
        int id = bookingService.create(resourceToCreate, userToCreate, startToCreate, durationToCreate);
        model.addAttribute("reservation",
                new Reservation(id, resourceToCreate, userToCreate, startToCreate, durationToCreate));
        return "reservations/created";
    }

    @GetMapping("/{id}")
    public String readById(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute("reservation", bookingService.readById(id));
        } catch (NotFoundReservationById e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "reservations/showById";
    }

    @GetMapping("/{id}/update")
    public String updateReservationPage(Model model, @PathVariable("id") int id) {
        Reservation reservation;
        try {
            reservation = bookingService.readById(id);
            model.addAttribute("reservation", reservation);
            model.addAttribute("resources", Resource.getResources());
            model.addAttribute("user", reservation.getUser().getUserName());
            model.addAttribute("start", reservation.getStart());
            model.addAttribute("duration", reservation.getDuration());
        } catch (NotFoundReservationById e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "reservations/update";
    }

    @PostMapping("/{id}/update")
    public String updateReservation(@RequestParam("resource") String resource,
                                    @RequestParam("user.userName") String user,
                                    @RequestParam("start") String start,
                                    @RequestParam("duration") String duration,
                                    @PathVariable("id") int id, Model model) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setResource(Resource.valueOf(resource));
        reservation.setUser(new User(user));
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
    public String deleteReservation(@PathVariable("id") int id, Model model) {
        String message = "Reservation with ID=" + id + " deleted";
        final boolean deleted = bookingService.delete(id);
        if (deleted) model.addAttribute("deleted", message);
        return "reservations/homePage";
    }

    @ExceptionHandler(NotPossibleAddBookingWithThisDateAndTime.class)
    public String handlerException(NotPossibleAddBookingWithThisDateAndTime e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "redirect:/reservations/new?m=" + e.getMessage();
    }
}
