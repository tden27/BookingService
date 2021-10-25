package com.github.tden27.bookingService.dao;

import com.github.tden27.bookingService.model.Reservation;
import com.github.tden27.bookingService.model.Resource;

import java.time.LocalDateTime;

public interface ReservationDao {

    /**
     * Читает из базы данных запись о брони по ID
     * @param id - id брони
     * @return - запись брони с указанным id
     */
    Reservation readById(int id);

    /**
     * Создает новую запись брони в базе данных
     * @param resource - ресурс для бронирования
     * @param user - имя пользователя
     * @param start - дата и время начала брони
     * @param duration - продолжительность брони в мин.
     * @return - id созданной записи брони
     */
    int crete(Resource resource, String user, LocalDateTime start, int duration);

    /**
     * Обновляет запись в базе данных
     * @param id - id обновляемой записи
     * @param reservation - обновленная запись брони
     * @return - true усли успешно, иначе false
     */
    boolean update(int id, Reservation reservation);

    /**
     * Удаляет запись брони из базы данных по указанному id
     * @param id - id удаляемой записи
     * @return - true усли успешно, иначе false
     */
    boolean delete(int id);

    /**
     * Поиск ближайшей предыдущей по дате записи
     * @param resource - ресурс по которому производится поиск
     * @param start - дата и время начала предыдущей записи
     * @return - запись брони предыдущей записи
     */
    Reservation searchClosestPreviousReservation (Resource resource, LocalDateTime start);

    /**
     * Поиск ближайшей следующей по дате записи
     * @param resource - ресурс по которому производится поиск
     * @param start - дата и время начала следующей записи
     * @return - запись брони следующей записи
     */
    Reservation searchClosestNextReservation (Resource resource, LocalDateTime start);
}