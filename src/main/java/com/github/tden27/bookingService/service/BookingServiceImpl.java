package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.dao.ReservationDao;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingServiceImpl implements BookingService {

    private final ReservationDao reservationDao;

    @Autowired
    public BookingServiceImpl(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public int create(Resource resource, String user, LocalDateTime start, int duration) {
        int result = 0;
        Reservation closestPreviousReservation = reservationDao.searchClosestPreviousReservation(resource, start);
        boolean isPreviousPostEarlier = closestPreviousReservation == null || start.isAfter(closestPreviousReservation.getStart().plusMinutes(closestPreviousReservation.getDuration()));
        if (isPreviousPostEarlier) {
            Reservation closestNextReservation = reservationDao.searchClosestNextReservation(resource, start);
            boolean isNextPostLater = closestNextReservation == null || closestNextReservation.getStart().isBefore(start.plusMinutes(duration));
            if (isNextPostLater) result = reservationDao.crete(resource, user, start, duration);
        }
        return result;
    }

    @Override
    public Reservation read(int id) {
        return reservationDao.readById(id);
    }

    @Override
    public boolean update(Reservation reservation, int id) {
        return reservationDao.update(id, reservation);
    }

    @Override
    public boolean delete(int id) {
        return reservationDao.delete(id);
    }
}
