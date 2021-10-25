package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.service.BookingService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new ReservationController(bookingService)).build();
    }

    @Test
    void createTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/new").accept(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void readByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/{id}", 16).
                        accept(MediaType.TEXT_HTML)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/update", 16).accept(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    void deleteReservationTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/delete", 20).accept(MediaType.APPLICATION_JSON)).
//                andExpect(MockMvcResultMatchers.status().isOk());
//    }
}