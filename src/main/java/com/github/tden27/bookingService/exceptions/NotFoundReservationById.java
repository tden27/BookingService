package com.github.tden27.bookingService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found reservation by ID")
public class NotFoundReservationById extends RuntimeException{

    public NotFoundReservationById(String message) {
        super(message);
    }
}
