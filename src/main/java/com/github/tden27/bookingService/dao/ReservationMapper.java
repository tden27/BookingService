package com.github.tden27.bookingService.dao;

import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationMapper implements RowMapper<Reservation> {
    @Override
    public Reservation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Reservation reservation = new Reservation();

        User user = new User(resultSet.getString("user_name")/*,
                resultSet.getString("login"),
                resultSet.getString("password")*/);

        reservation.setId(resultSet.getInt("id"));
        reservation.setResource(Resource.valueOf(resultSet.getString("resource")));
        reservation.setUser(user);
        reservation.setStart(resultSet.getTimestamp("start").toLocalDateTime());
        reservation.setDuration(resultSet.getInt("duration"));
        return reservation;
    }
}
