package com.github.tden27.bookingService.dao;

import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class ReservationDaoJdbcTemplateImpl implements ReservationDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation readById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM reservations WHERE id=?",
                new ReservationMapper(), id);
    }

    @Override
    public int create(Resource resource, String user, LocalDateTime start, int duration) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservations(resource, user_name, start, duration) VALUES (?, ?, ?, ?)",
                    new String[] {"id"});
            ps.setString(1, resource.toString());
            ps.setString(2, user);
            ps.setTimestamp(3, Timestamp.valueOf(start));
            ps.setInt(4, duration);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public boolean update(int id, Reservation reservation) {
        int result = jdbcTemplate.update("UPDATE reservations SET resource=?, user_name=?, start=?, duration=? WHERE id=?",
                reservation.getResource().toString(),
                reservation.getUser(),
                Timestamp.valueOf(reservation.getStart()),
                reservation.getDuration(),
                id);
        return result > 0;
    }

    @Override
    public boolean delete(int id) {
        int result = jdbcTemplate.update("DELETE FROM reservations WHERE id=?", id);
        return result > 0;
    }

    // Поиск ближайшей предыдущей брони
    public Reservation searchClosestPreviousReservation (Resource resource, LocalDateTime start) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM reservations WHERE resource=? AND start<=? ORDER BY start DESC LIMIT 1",
                    new ReservationMapper(), resource.toString(), Timestamp.valueOf(start));

        } catch (DataAccessException e) {
            return null;
        }
    }

    // Поиск ближайшей следующей брони
    public Reservation searchClosestNextReservation (Resource resource, LocalDateTime start) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM reservations WHERE resource=? AND start>=? ORDER BY start LIMIT 1",
                    new ReservationMapper(), resource.toString(), Timestamp.valueOf(start));
        } catch (DataAccessException e) {
            return null;
        }
    }
}
