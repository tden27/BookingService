package com.github.tden27.bookingService.exceptions;

public class NotPossibleAddBookingWithThisDateAndTime extends Exception {

    public NotPossibleAddBookingWithThisDateAndTime(String message) {
        super(message);
    }
}
