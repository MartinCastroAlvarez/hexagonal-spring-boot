package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.Schedule;
import com.martincastroalvarez.hex.hex.domain.models.Pto;
import com.martincastroalvarez.hex.hex.domain.models.Work;
import java.util.List;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.DayOfWeek;

public interface ScheduleService {
    Schedule getSchedule(Integer scheduleId) throws HexagonalEntityNotFoundException;
    Work getWork(Integer workId) throws HexagonalEntityNotFoundException;
    Pto getPto(Integer ptoId) throws HexagonalEntityNotFoundException;
    List<Schedule> listSchedule(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<Schedule> listSchedule(Integer userId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalEntityNotFoundException;
    Schedule addNewScheduleToUser(Integer userId, Time start, Time end, DayOfWeek dayOfWeek) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    void removeScheduleFromUser(Integer userId, Integer scheduleId) throws HexagonalEntityNotFoundException;
    List<Pto> listTimeOff(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<Pto> listTimeOff(int userId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalEntityNotFoundException;
    Pto addNewTimeOffToUser(int userId, LocalDate day, Pto.Type type) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    void removeTimeOffFromUser(int userId, int timeOffId) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    Work checkIn(int userId) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    Work checkOut(int userId) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    List<Work> listWork(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<Work> listWork(int userId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalValidationException;
}