package com.github.tden27.bookingService.config;

import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceApiAdvice {

    @ExceptionHandler(NotPossibleAddBookingWithThisDateAndTime.class)
    public String handlerException(NotPossibleAddBookingWithThisDateAndTime e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "redirect:/new?m=" + e.getMessage();
    }
}
