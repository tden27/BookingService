package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByResource;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByUser;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import com.github.tden27.bookingService.repository.ReservationRepository;
import com.github.tden27.bookingService.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long create(Resource resource, User user, LocalDateTime start, int duration) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(null, resource, start, duration)) {
            Reservation reservation = new Reservation(resource, user, start, duration);
            return reservationRepository.save(reservation).getId();
        } else
            throw new NotPossibleAddBookingWithThisDateAndTime("It is not possible to add a booking with this date and this time");
    }

    @Override
    @Transactional
    public Reservation update(Reservation reservation, Long id) throws NotPossibleAddBookingWithThisDateAndTime {
        if (isAbilityToAddReservation(id, reservation.getResource(), reservation.getStart(), reservation.getDuration())) {
            Reservation reservationToUpdate = reservationRepository.getOne(id);
            reservationToUpdate.setResource(reservation.getResource());
            reservationToUpdate.setStart(reservation.getStart());
            reservationToUpdate.setDuration(reservation.getDuration());
            return reservationRepository.save(reservationToUpdate);
        } else
            throw new NotPossibleAddBookingWithThisDateAndTime("It is not possible to add a booking with this date and this time");
    }

    @Override
    public boolean delete(Long id) {
        reservationRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean isAbilityToAddReservation(Long id, Resource resource, LocalDateTime start, int duration) {
        Reservation closestPreviousReservation = reservationRepository.findFirstByResourceAndStartBeforeOrderByStartDesc(resource, start);
        if (closestPreviousReservation == null ||
                start.isAfter(closestPreviousReservation.getStart().plusMinutes(closestPreviousReservation.getDuration())) ||
                start.equals(closestPreviousReservation.getStart().plusMinutes(closestPreviousReservation.getDuration()))) {
            Reservation closestNextReservation = reservationRepository.findFirstByResourceAndStartAfterOrderByStart(resource, start);
            return closestNextReservation == null ||
                    closestNextReservation.getStart().isAfter(start.plusMinutes(duration)) ||
                    closestNextReservation.getStart().equals(start.plusMinutes(duration));
        }
        return false;
    }

    @Override
    public Reservation readById(Long id) throws NotFoundReservationById {
            Reservation reservation = reservationRepository.readById(id);
            if (reservation != null) return reservation;
            else throw new NotFoundReservationById("Not found reservation by ID - " + id);
    }

    @Override
    public List<Reservation> readByUser(String username) throws NotFoundReservationsByUser {
        List<User> usersToSearch = userRepository.findByName(username);
        List<Reservation> result = new ArrayList<>();
        for (User user : usersToSearch) {
            result.addAll(reservationRepository.readAllByUser(user));
        }
        if (result.size() == 0) throw new NotFoundReservationsByUser("Not found reservation by user - " + username);
        return result;
    }

    @Override
    public List<Reservation> readByResource(String resource) throws NotFoundReservationsByResource {
        List<Reservation> result = reservationRepository.readAllByResource(Resource.valueOf(resource));
        if (result.size() == 0)
            throw new NotFoundReservationsByResource("Not found reservation by resource - " + resource);
        return result;
    }
}
