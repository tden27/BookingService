package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.service.BookingService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationController controller;

    @MockBean
    private BookingService bookingService;

//    @BeforeEach
//    public void setUp() {
//        mockMvc = standaloneSetup(new ReservationController(bookingService)).build();
//    }

    @Test
    void homePageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/reservations/homePage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Создать бронирование")))
                .andExpect(content().string(containsString("Поиск брони по ID")));
    }

    @Test
    @Transactional
    @Ignore
    void createTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/new").accept(MediaType.TEXT_HTML)).
                andExpect(status().isOk());
    }

    @Test
    void readByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/{id}", 5))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reservation{id=5, resource=DOCTOR, user=Den, start=2021-11-11T20:25, duration=20 minutes}")));
    }

    @Test
    @Transactional
    @Ignore
    void updateReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/update", 5).
                        accept(MediaType.TEXT_HTML)).
                andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Ignore
    void deleteReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/delete", 5).accept(MediaType.TEXT_HTML)).
                andExpect(status().isOk());
    }
}