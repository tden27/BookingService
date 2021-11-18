package com.github.tden27.bookingService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found reservation by user")
public class NotFoundReservationsByUser extends RuntimeException{

    public NotFoundReservationsByUser(String message) {
        super(message);
    }
}
