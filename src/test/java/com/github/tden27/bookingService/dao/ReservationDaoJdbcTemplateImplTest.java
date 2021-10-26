package com.github.tden27.bookingService.dao;

import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

class ReservationDaoJdbcTemplateImplTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Test
    void searchClosestPreviousReservationTest() {
        Reservation actual = new Reservation(28, Resource.MAIL_POST, "Egor", LocalDateTime.of(2021, 10, 30, 14, 50), 900);
        Reservation expected = jdbcTemplate.queryForObject("SELECT * FROM reservations WHERE resource=MAIL_POST AND start<=timestamp '2021-10-30 14:55' ORDER BY start DESC LIMIT 1",
                new ReservationMapper());
        Assert.assertEquals(actual, expected);
    }

    @Test
    void searchClosestNextReservationTest() {
    }
}