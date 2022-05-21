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
    public String registration(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("name", "name");
        model.addAttribute("username", "username");
        model.addAttribute("password", "password");
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute @Valid User user,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!userService.addUser(user)) {
            return "registration";
        }
        return "redirect:/login";
    }
}
