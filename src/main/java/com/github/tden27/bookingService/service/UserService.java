package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.model.Role;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BookingService bookingService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookingService = bookingService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return true;
    }

    public boolean isAccess(User user, Long id) {
        return user.getUsername().equals(bookingService.readById(id).getUser().getUsername());
    }
}
