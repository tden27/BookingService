package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          Model model) {
        User newUser = new User(username, password, name);
        if (!userService.addUser(newUser)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }
}
