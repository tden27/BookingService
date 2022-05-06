package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.dao.ReservationDao;
import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByResource;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByUser;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final ReservationDao reservationDao;

    private final ReservationRepository reservationRepository;

    public BookingServiceImpl(ReservationDao reservationDao, ReservationRepository reservationRepository) {
        this.reservationDao = reservationDao;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Long create(Resource resource, User user, LocalDateTime start, int duration) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(resource, start, duration)) {
            Reservation reservation = new Reservation(resource, user, start, duration);
            return reservationRepository.save(reservation).getId();
        }
        else throw new NotPossibleAddBookingWithThisDateAndTime("It is not possible to add a booking with this date");
    }

    @Override
    public Reservation readById(Long id) throws NotFoundReservationById {
        try {
            return reservationRepository.readById(id);
        } catch (Exception e) {
            throw new NotFoundReservationById("Not found reservation by ID - " + id);
        }
    }

    @Override
    @Transactional
    public boolean update(Reservation reservation, Long id) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(reservation.getResource(), reservation.getStart(), reservation.getDuration()))
            return reservationDao.update(id, reservation);
        else throw new NotPossibleAddBookingWithThisDateAndTime("It is not possible to add a booking with this date");
    }

    @Override
    public boolean delete(Long id) {
        reservationRepository.deleteById(id);
        return true;
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
        List<Reservation> result = reservationRepository.readAllByUser(user);
        if (result.size() == 0) throw new NotFoundReservationsByUser("Not found reservation by user - " + user);
        return result;
    }

    @Override
    public List<Reservation> readByResource(String resource) throws NotFoundReservationsByResource {
        List<Reservation> result = reservationRepository.readAllByResource(Resource.valueOf(resource));
        if (result.size() == 0) throw new NotFoundReservationsByResource("Not found reservation by resource - " + resource);
        return result;
    }
}
