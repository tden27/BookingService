package com.github.tden27.bookingService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found reservation by resource")
public class NotFoundReservationsByResource extends RuntimeException{

    public NotFoundReservationsByResource(String message) {
        super(message);
    }
}
