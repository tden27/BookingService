package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.model.Role;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          Model model) {
        User userFromDB = userRepository.findByUsername(username);
        if (userFromDB != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        User newUser = new User(username, password, name);
        newUser.setActive(true);
        newUser.setRoles(Collections.singleton(Role.USER));
        userRepository.save(newUser);
        return "redirect:/login";
    }
}
