package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByResource;
import com.github.tden27.bookingService.exceptions.NotFoundReservationsByUser;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;
import com.github.tden27.bookingService.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookingService {

    /**
     * Бронирует ресурс
     * @param resource - русурс, который бронируется
     * @param user - пользователь забронировавший ресурс
     * @param start - дата начала бронирования
     * @param duration - продолжительность бронирования
     * @return - ID брони
     * @throws NotPossibleAddBookingWithThisDateAndTime - исключение о невозможности добавления записи на указанные дату
     * время и продолжительность
     */
    Long create(Resource resource, User user, LocalDateTime start, int duration) throws NotPossibleAddBookingWithThisDateAndTime;

    /**
     * Возвращает запись брони по ID
     * @param id - id брони
     * @return - запись брони с заданным ID
     * @throws NotFoundReservationById - исключение о невозможности найти записи с указанным ID
     */
    Reservation readById(Long id) throws NotFoundReservationById;

    /**
     * Обновляет запись брони с заданным ID,
     * в соответствии с переданной бронью
     * @param reservation - клиент в соответсвии с которым нужно обновить данные
     * @param id - id записи брони которую нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(Reservation reservation, Long id) throws NotPossibleAddBookingWithThisDateAndTime;

    /**
     * Освобождает ресурс по идентификатору брони
     * @param id - идентификатор брони
     * @return true если освобождение прошло успешно, иначе false
     * @throws NotPossibleAddBookingWithThisDateAndTime - исключение о невозможности добавления записи на указанные дату
     * время и продолжительность
     */
    boolean delete(Long id);

    /**
     * Проверяет возможность добавления бронирования по указанной дате и времени,
     * сравнивая с предыдущей и последующей записями бронирования
     * @param resource - ресурс который бронируется
     * @param start - дата и время начала бронирования
     * @param duration - продолжительность бронирования
     * @return - true если можно добавить такую запись, false если нет
     */
    boolean isAbilityToAddReservation(Resource resource, LocalDateTime start, int duration);

    /**
     * Возвращает список записей о бронировании у данного пользователя
     * @param user - пользователь забронировавший ресурс
     * @return - список записей о бронировании у данного пользователя
     * @throws NotFoundReservationsByUser - исключение о невозможности найти записей с указанным именем пользователя
     */
    List<Reservation> readByUser(User user) throws NotFoundReservationsByUser;

    /**
     * Возвращает список записей о бронировании по указанному ресурсу
     * @param resource - ресурс который забронирован
     * @return - список записей о бронировании по указанному ресурсу
     * @throws NotFoundReservationsByResource - исключение о невозможности найти записей с указанным ресурсом
     */
    List<Reservation> readByResource(String resource) throws NotFoundReservationsByResource;
}
