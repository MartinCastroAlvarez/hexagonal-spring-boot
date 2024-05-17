package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.*;
import com.martincastroalvarez.hex.hex.domain.ports.out.*;
import com.martincastroalvarez.hex.hex.domain.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleApplication extends HexagonalApplication implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PtoRepository ptoRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UserRepository userRepository;

    private final List<String>SCHEDULE_SORT_KEYS = List.of("id", "dayOfWeek", "user");
    private final List<String>PTO_SORT_KEYS = List.of("id", "day", "user");
    private final List<String>WORK_SORT_KEYS = List.of("id", "user", "startTime", "endTime");

    @Override
    public Schedule getSchedule(Integer scheduleId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting schedule rule %d", scheduleId));
        return scheduleRepository.get(scheduleId).orElseThrow(() -> new ScheduleNotFoundException());
    }

    @Override
    public Work getWork(Integer workId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting work %d", workId));
        return workRepository.get(workId).orElseThrow(() -> new WorkNotFoundException());
    }

    @Override
    public Pto getPto(Integer ptoId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting time off %d", ptoId));
        return ptoRepository.get(ptoId).orElseThrow(() -> new PtoNotFoundException());
    }

    @Override
    public List<Schedule> listSchedule(Integer page, Integer size, String sort, Boolean asc) {
        logger.info(String.format("Listing schedule rules. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        return scheduleRepository.get(getPageRequest(page, size, sort, asc, SCHEDULE_SORT_KEYS)).getContent();
    }

    @Override
    public List<Schedule> listSchedule(Integer userId, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Listing schedule rules for user %d. Page: %d, Size: %d, Sort: %s, Ascending: %b", userId, page, size, sort, asc));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        return scheduleRepository.get(user, getPageRequest(page, size, sort, asc, SCHEDULE_SORT_KEYS)).getContent();
    }

    @Override
    public Schedule addNewScheduleToUser(Integer userId, Time start, Time end, DayOfWeek dayOfWeek) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Adding new schedule to user %d. Start: %s, End: %s, DayOfWeek: %s", userId, start, end, dayOfWeek));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        Schedule schedule = new Schedule();
        schedule.setUser(user);
        schedule.setStartTime(start);
        schedule.setEndTime(end);
        schedule.setDayOfWeek(dayOfWeek);
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Pto> listTimeOff(LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing time off between %s and %s", start, end));
        if (start == null || end == null)
            return ptoRepository.get(getPageRequest(page, size, sort, asc, PTO_SORT_KEYS)).getContent();
        return ptoRepository.get(start, end, getPageRequest(page, size, sort, asc, PTO_SORT_KEYS)).getContent();
    }

    @Override
    public List<Pto> listTimeOff(int userId, LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalEntityNotFoundException {
        logger.info(String.format("Listing time off for user %d between %s and %s", userId, start, end));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        if (start == null || end == null)
            return ptoRepository.get(user, getPageRequest(page, size, sort, asc, PTO_SORT_KEYS)).getContent();
        return ptoRepository.get(user, start, end, getPageRequest(page, size, sort, asc, PTO_SORT_KEYS)).getContent();
    }

    @Override
    public Pto addNewTimeOffToUser(int userId, LocalDate day, Pto.Type type) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Adding new time off to user %d on date %s and type %s", userId, day, type));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        Pto pto = new Pto();
        pto.setUser(user);
        pto.setType(type);
        pto.setDay(day);
        return ptoRepository.save(pto);
    }

    @Override
    public void removeScheduleFromUser(Integer userId, Integer scheduleId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Removing schedule %d from user %d", scheduleId, userId));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        Schedule schedule = scheduleRepository.get(scheduleId).orElseThrow(() -> new ScheduleNotFoundException());
        logger.info(String.format("Schedule user: %s", schedule.getUser().getName()));
        if (schedule.getUser().getId() != user.getId())
            throw new ScheduleNotFoundException();
        scheduleRepository.delete(schedule.getId());
    }

    @Override
    public void removeTimeOffFromUser(int userId, int timeOffId) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Removing time off %d from user %d", timeOffId, userId));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        Pto pto = ptoRepository.get(timeOffId).orElseThrow(() -> new PtoNotFoundException());
        logger.info(String.format("PTO user: %s", pto.getUser().getName()));
        if (pto.getUser().getId() != user.getId())
            throw new PtoNotFoundException();
        ptoRepository.delete(pto.getId());
    }

    @Override
    public Work checkIn(int userId) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Checking in user %d", userId));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        Optional<Work> lastCheckIn = workRepository.get(user, getPageRequest(0, 1, "id", false, WORK_SORT_KEYS)).stream().findFirst();
        if (lastCheckIn.isPresent()) {
            Work work = lastCheckIn.get();
            logger.info(String.format("Found last work: %s", work));
            if (work.getEndTime() == null)
                throw new AlreadyWorkingException();
        }
        LocalDateTime now = LocalDateTime.now();
        Work work = new Work();
        work.setUser(user);
        work.setStartTime(now);
        return workRepository.save(work);
    }

    @Override
    public Work checkOut(int userId) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Checking out user %d", userId));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        Optional<Work> lastCheckIn = workRepository.get(user, getPageRequest(0, 1, "id", false, WORK_SORT_KEYS)).stream().findFirst();
        if (!lastCheckIn.isPresent())
            throw new NotWorkingException();
        Work work = lastCheckIn.get();
        logger.info(String.format("Found last work: %s", work));
        if (work.getEndTime() != null)
            throw new NotWorkingException();
        LocalDateTime now = LocalDateTime.now();
        work.setEndTime(now);
        return workRepository.save(work);
    }

    @Override
    public List<Work> listWork(LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing checked-in time between %s and %s", start, end));
        if (start == null || end == null)
            return workRepository.get(getPageRequest(page, size, sort, asc, WORK_SORT_KEYS)).getContent();
        return workRepository.get(start, end, getPageRequest(page, size, sort, asc, WORK_SORT_KEYS)).getContent();
    }

    @Override
    public List<Work> listWork(int userId, LocalDateTime start, LocalDateTime end, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException, HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Listing checked-in time for user %d between %s and %s", userId, start, end));
        User user = userRepository.get(userId).orElseThrow(() -> new UserNotFoundException());
        if (start == null || end == null)
            return workRepository.get(user, getPageRequest(page, size, sort, asc, WORK_SORT_KEYS)).getContent();
        return workRepository.get(user, start, end, getPageRequest(page, size, sort, asc, WORK_SORT_KEYS)).getContent();
    }
}
