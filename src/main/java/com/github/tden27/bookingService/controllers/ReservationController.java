package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.BookingService;
import com.github.tden27.bookingService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAllReservations(@AuthenticationPrincipal User user) {
        List<Reservation> reservations = bookingService.readByUser(user.getName());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@AuthenticationPrincipal User user,
                                                @PathVariable("id") Long id) {
        if (userService.isAccess(user, id)) {
            Reservation reservation = bookingService.readById(id);
            return ResponseEntity.ok(reservation);
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody @Valid Reservation reservation,
                                    BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        Long reservationId = bookingService.create(reservation);
        return ResponseEntity.ok(reservationId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@ModelAttribute @Valid Reservation reservation,
                                               BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        Reservation updatedReservation = bookingService.update(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.delete(id));
    }
}
