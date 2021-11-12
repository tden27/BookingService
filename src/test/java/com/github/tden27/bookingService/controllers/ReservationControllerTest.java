package com.github.tden27.bookingService.controllers;

import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.service.BookingService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationController controller;

    @Autowired
    private BookingService bookingService;

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
    void createTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/new")
                        .param("resource", String.valueOf(Resource.DOCTOR))
                        .param("user", "Viktor")
                        .param("start", "2021-11-25T12:30")
                        .param("duration", "30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("resource=DOCTOR, user=Viktor, start=2021-11-25T12:30, duration=30 minutes}")));
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
    void updateReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/update", 5)
                        .param("resource", String.valueOf(Resource.CONFERENCE_HALL))
                        .param("user", "Aleksey")
                        .param("start", "2021-12-31T10:00")
                        .param("duration", "120"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reservation{id=5, resource=CONFERENCE_HALL, user=Aleksey, start=2021-12-31T10:00, duration=120 minutes}")));
    }

    @Test
    @Transactional
    void deleteReservationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations/{id}/delete", 5).accept(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reservation with ID=5 deleted")));
    }
}