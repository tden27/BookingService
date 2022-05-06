package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping
public class ReservationController {

    private final BookingService bookingService;

    public ReservationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("name", user.getName());
        return "/home";
    }

    @GetMapping("/new")
    public String createReservationPage(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("resources", Resource.getResources());
        model.addAttribute("user", "");
        model.addAttribute("start", LocalDateTime.now());
        model.addAttribute("duration", 0);
        if (!model.containsAttribute("error")) {
            model.addAttribute("error");
        }
        return "/new";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal User user,
                         @RequestParam("resource") String resource,
                         @RequestParam("user") String userName,
                         @RequestParam("start") String start,
                         @RequestParam("duration") String duration,
                         Model model) throws NotPossibleAddBookingWithThisDateAndTime {
        Resource resourceToCreate = Resource.valueOf(resource);
        LocalDateTime startToCreate = LocalDateTime.parse(start);
        int durationToCreate = Integer.parseInt(duration);
        Long id = bookingService.create(resourceToCreate, user, startToCreate, durationToCreate);
        model.addAttribute("reservation",
                new Reservation(id, resourceToCreate, user, startToCreate, durationToCreate));
        return "/created";
    }

    @GetMapping("/reservation/{id}")
    public String readById(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("reservation", bookingService.readById(id));
        } catch (NotFoundReservationById e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "/showById";
    }

    @GetMapping("/reservation/{id}/update")
    public String updateReservationPage(Model model, @PathVariable("id") Long id) {
        Reservation reservation;
        try {
            reservation = bookingService.readById(id);
            model.addAttribute("reservation", reservation);
            model.addAttribute("resources", Resource.getResources());
            model.addAttribute("user", reservation.getUser().getName());
            model.addAttribute("start", reservation.getStart());
            model.addAttribute("duration", reservation.getDuration());
        } catch (NotFoundReservationById e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "/update";
    }

    @PostMapping("/reservation/{id}/update")
    public String updateReservation(@RequestParam("resource") String resource,
                                    @RequestParam("user.userName") String user,
                                    @RequestParam("start") String start,
                                    @RequestParam("duration") String duration,
                                    @PathVariable("id") Long id, Model model) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setResource(Resource.valueOf(resource));
        reservation.setUser(new User());
        reservation.setStart(LocalDateTime.parse(start));
        reservation.setDuration(Integer.parseInt(duration));
        try {
            bookingService.update(reservation, id);
            model.addAttribute("reservation", reservation);
        } catch (NotPossibleAddBookingWithThisDateAndTime e) {
            model.addAttribute("reservation", e.getMessage());
        }
        return "/updated";
    }

    @PostMapping("/reservation/{id}/delete")
    public String deleteReservation(@PathVariable("id") Long id, Model model) {
        String message = "Reservation with ID=" + id + " deleted";
        final boolean deleted = bookingService.delete(id);
        if (deleted) model.addAttribute("deleted", message);
        return "/home";
    }

    @ExceptionHandler(NotPossibleAddBookingWithThisDateAndTime.class)
    public String handlerException(NotPossibleAddBookingWithThisDateAndTime e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "redirect:/new?m=" + e.getMessage();
    }
}
