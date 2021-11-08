package com.github.tden27.bookingService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tden27.bookingService.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new ReservationController(bookingService)).build();
    }

    @Test
    @Transactional
    void createTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/new").accept(MediaType.TEXT_HTML)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void readByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/{id}", 2).
                        accept(MediaType.TEXT_HTML)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void updateReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/update", 2).accept(MediaType.TEXT_HTML)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void deleteReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/delete", 2).accept(MediaType.TEXT_HTML)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }
}