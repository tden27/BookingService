package com.github.tden27.bookingService.service;

import com.github.tden27.bookingService.exceptions.NotFoundReservationById;
import com.github.tden27.bookingService.exceptions.NotPossibleAddBookingWithThisDateAndTime;
import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;

import java.time.LocalDateTime;

public interface BookingService {

    /**
     * Бронирует ресурс
     * @param resource - русурс, который бронируется
     * @param user - пользователь забронировавший ресурс
     * @param start - дата начала бронирования
     * @param duration - продолжительность бронирования
     * @return - ID брони
     */
    int create(Resource resource, String user, LocalDateTime start, int duration) throws NotPossibleAddBookingWithThisDateAndTime;

    /**
     * Возвращает запись брони по ID
     * @param id - id брони
     * @return - запись брони с заданным ID
     */
    Reservation read(int id) throws NotFoundReservationById;

    /**
     * Обновляет запись брони с заданным ID,
     * в соответствии с переданной бронью
     * @param reservation - клиент в соответсвии с которым нужно обновить данные
     * @param id - id записи брони которую нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(Reservation reservation, int id) throws NotPossibleAddBookingWithThisDateAndTime;

    /**
     * Освобождает ресурс по идентификатору брони
     * @param id - идентификатор брони
     * @return true если освобождение прошло успешно, иначе false
     */
    boolean delete(int id);

    /**
     * Проверяет возможность добавления бронирования по указанной дате и времени,
     * сравнивая с предыдущей и последующей записями бронирования
     * @param resource - ресурс который бронируется
     * @param start - дата и время начала бронирования
     * @param duration - продолжительность бронирования
     * @return - true если можно добавить такую запись, false если нет
     */
    boolean isAbilityToAddReservation(Resource resource, LocalDateTime start, int duration);
}
