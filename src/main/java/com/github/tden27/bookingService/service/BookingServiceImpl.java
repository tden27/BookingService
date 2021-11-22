package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.dao.ReservationDao;
import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByResource;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByUser;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final ReservationDao reservationDao;

    @Autowired
    public BookingServiceImpl(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    @Transactional
    public int create(Resource resource, User user, LocalDateTime start, int duration) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(resource, start, duration)) return reservationDao.create(resource, user, start, duration);
        else throw new NotPossibleAddBookingWithThisDateAndTime("It is not possible to add a booking with this date");
    }

    @Override
    public Reservation readById(int id) throws NotFoundReservationById {
        try {
            return reservationDao.readById(id);
        } catch (Exception e) {
            throw new NotFoundReservationById("Not found reservation by ID - " + id);
        }
    }

    @Override
    @Transactional
    public boolean update(Reservation reservation, int id) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(reservation.getResource(), reservation.getStart(), reservation.getDuration()))
            return reservationDao.update(id, reservation);
        else throw new NotPossibleAddBookingWithThisDateAndTime("It is not possible to add a booking with this date");
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

    @Override
    public List<Reservation> readByUser(User user) throws NotFoundReservationsByUser {
        List<Reservation> result = reservationDao.readByUser(user);
        if (result.size() == 0) throw new NotFoundReservationsByUser("Not found reservation by user - " + user);
        return result;
    }

    @Override
    public List<Reservation> readByResource(String resource) throws NotFoundReservationsByResource {
        List<Reservation> result = reservationDao.readByResource(resource);
        if (result.size() == 0) throw new NotFoundReservationsByResource("Not found reservation by resource - " + resource);
        return result;
    }
}
