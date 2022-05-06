package com.github.tden27.bookingService.repository;

import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation readById(Long id);
    List<Reservation> readAllByUser(User user);
    List<Reservation> readAllByResource(Resource resource);
}
