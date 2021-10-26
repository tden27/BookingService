package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.dao.ReservationDao;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
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
    public int create(Resource resource, String user, LocalDateTime start, int duration) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(resource, start, duration)) return reservationDao.create(resource, user, start, duration);
        else throw new NotPossibleAddBookingWithThisDateAndTime("Не возможно добавить запись с такой датой");
    }

    @Override
    public Reservation read(int id) {
        return reservationDao.readById(id);
    }

    @Override
    public boolean update(Reservation reservation, int id) {
        if (isAbilityToAddReservation(reservation.getResource(), reservation.getStart(), reservation.getDuration()))
            return reservationDao.update(id, reservation);
        else return false;
    }

    @Override
    public boolean delete(int id) {
        return reservationDao.delete(id);
    }

    @Override
    public boolean isAbilityToAddReservation(Resource resource, LocalDateTime start, int duration) {
        Reservation closestPreviousReservation = reservationDao.searchClosestPreviousReservation(resource, start);
        if (closestPreviousReservation == null || start.isAfter(closestPreviousReservation.getStart().plusMinutes(closestPreviousReservation.getDuration()))) {
            Reservation closestNextReservation = reservationDao.searchClosestNextReservation(resource, start);
            return closestNextReservation == null || closestNextReservation.getStart().isAfter(start.plusMinutes(duration));
        }
        return false;
    }
}
