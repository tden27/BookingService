package com.github.tden27.bookingService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found reservation by user")
public class NotFoundReservationByUser extends RuntimeException{

    public NotFoundReservationByUser(String message) {
        super(message);
    }
}
