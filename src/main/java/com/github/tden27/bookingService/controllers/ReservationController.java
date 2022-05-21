package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.BookingService;
import com.github.tden27.bookingService.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping
public class ReservationController {

    private final BookingService bookingService;
    private final UserService userService;

    public ReservationController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "/home";
    }

    @GetMapping("/new")
    public String createReservationPage(@ModelAttribute Reservation reservation,
                                        Model model) {
        if (!model.containsAttribute("error")) {
            model.addAttribute("error");
        }
        return "/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute @Valid Reservation reservation,
                         BindingResult bindingResult,
                         Model model) throws NotPossibleAddBookingWithThisDateAndTime {
        if (bindingResult.hasErrors()) {
            return "/new";
        }
        bookingService.create(reservation);
        model.addAttribute("reservation", reservation);
        return "/created";
    }

    @GetMapping("/reservation/{id}")
    public String readById(@AuthenticationPrincipal User user,
                           @PathVariable("id") Long id,
                           Model model) {
        try {
            model.addAttribute("reservation", bookingService.readById(id));
            model.addAttribute("isAccess", userService.isAccess(user, id));
        } catch (NotFoundReservationById e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "/showById";
    }

    @GetMapping("/reservation/{id}/update")
    public String updateReservationPage(@PathVariable("id") Long id,
                                        Model model) {
        try {
            Reservation reservation = bookingService.readById(id);
            model.addAttribute("reservation", reservation);
        } catch (NotFoundReservationById e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "/update";
    }

    @PostMapping("/reservation/{id}/update")
    public String updateReservation(@ModelAttribute @Valid Reservation reservation,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            return "/update";
        }
        try {
            bookingService.update(reservation);
            model.addAttribute("reservation", reservation);
        } catch (NotPossibleAddBookingWithThisDateAndTime e) {
            model.addAttribute("reservation", e.getMessage());
        }
        return "/updated";
    }

    @PostMapping("/reservation/{id}/delete")
    public String deleteReservation(@PathVariable("id") Long id,
                                    Model model) {
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
