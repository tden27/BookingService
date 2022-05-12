package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByResource;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByUser;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class ReservationSearchController {

    private final BookingService bookingService;

    public ReservationSearchController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/searchById")
    public String searchByIdPage(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("id", 0);
        return "/search/searchById";
    }

    @PostMapping("/searchById")
    public String searchById(@RequestParam("id") String id, Model model) {
        Long idReservation = Long.parseLong(id);
        try {
            model.addAttribute("reservation", bookingService.readById(idReservation));
            model.addAttribute("id", id);
        } catch (NotFoundReservationById e) {
            model.addAttribute("reservation", new Reservation());
            model.addAttribute("id", 0);
            model.addAttribute("errorMessage", e.getMessage());
            return "/search/searchById";
        }
        return "redirect:/reservation/" + id;
    }

    @GetMapping("/searchByUser")
    public String searchByUser(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("user", "");
        return "/search/searchByUser";
    }

    @PostMapping("/searchByUser")
    public String searchByUser(@RequestParam("user") String user, Model model) {
        try {
            model.addAttribute("reservations", bookingService.readByUser(user));
        } catch (NotFoundReservationsByUser e) {
            model.addAttribute("reservation", new Reservation());
            model.addAttribute("user", "");
            model.addAttribute("errorMessage", e.getMessage());
            return "/search/searchByUser";
        }
        return "/showReservations";
    }

    @GetMapping("/searchByResource")
    public String searchByResource(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("resources", Resource.getResources());
        return "/search/searchByResource";
    }

    @PostMapping("/searchByResource")
    public String searchByResource(@RequestParam("resource") String resource, Model model) {
        try {
            if (resource.equals("NONE")) throw new NotFoundReservationsByResource("Выберите ресурс");
            model.addAttribute("reservations", bookingService.readByResource(resource));
        } catch (NotFoundReservationsByResource e) {
            model.addAttribute("reservation", new Reservation());
            model.addAttribute("resources", Resource.getResources());
            model.addAttribute("errorMessage", e.getMessage());
            return "/search/searchByResource";
        }
        return "/showReservations";
    }
}
