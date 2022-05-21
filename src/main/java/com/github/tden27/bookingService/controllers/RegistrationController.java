package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute User user) {
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute @Valid User user,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        model.addAttribute("user", user);
        if (!userService.addUser(user)) {
            return "registration";
        }
        return "redirect:/login";
    }
}
