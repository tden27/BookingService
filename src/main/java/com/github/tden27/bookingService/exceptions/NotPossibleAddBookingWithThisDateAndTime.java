package com.github.tden27.bookingService.exceptions;

public class NotPossibleAddBookingWithThisDateAndTime extends Throwable {
    private String message;

    public NotPossibleAddBookingWithThisDateAndTime(String message) {
        super(message);
        this.message = message;
    }
}
