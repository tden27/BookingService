package com.github.tden27.bookingService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "It is not possible to add a booking with this date")
public class NotFoundReservationById extends Exception{

    public NotFoundReservationById(String message) {
        super(message);
    }
}
