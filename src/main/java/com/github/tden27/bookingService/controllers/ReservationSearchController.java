package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotFoundReservationByUser;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reservations")
public class ReservationSearchController {

    private final BookingService bookingService;

    @Autowired
    public ReservationSearchController(BookingService bookingService) {
        this.bookingService = bookingService;
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
            model.addAttribute("reservation", new Reservation());
            model.addAttribute("id", 0);
            model.addAttribute("errorMessage", e.getMessage());
            return "reservations/searchById";
        }
        return "reservations/showById";
    }

    @GetMapping("/searchByUser")
    public String searchByUser(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("user", "");
        return "reservations/searchByUser";
    }

    @PostMapping("/searchByUser")
    public String searchByUser(@RequestParam("user") String user, Model model) {
        try {
            model.addAttribute("reservations", bookingService.read(user));
        } catch (NotFoundReservationByUser e) {
            model.addAttribute("reservation", new Reservation());
            model.addAttribute("user", "");
            model.addAttribute("errorMessage", e.getMessage());
            return "reservations/searchByUser";
        }
        return "reservations/showByUser";
    }
}
