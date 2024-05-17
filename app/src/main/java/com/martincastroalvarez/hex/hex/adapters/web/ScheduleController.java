package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.adapters.dto.PtoDTO;
import com.martincastroalvarez.hex.hex.adapters.dto.ScheduleDTO;
import com.martincastroalvarez.hex.hex.adapters.dto.WorkDTO;
import com.martincastroalvarez.hex.hex.adapters.mappers.PtoMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.ScheduleMapper;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.adapters.mappers.WorkMapper;
import com.martincastroalvarez.hex.hex.domain.services.ScheduleService;
import com.martincastroalvarez.hex.hex.domain.models.Pto;
import com.martincastroalvarez.hex.hex.domain.models.Schedule;
import com.martincastroalvarez.hex.hex.domain.models.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ScheduleController extends HexagonalController {
    @Autowired
    private ScheduleService scheduleApplication;

    @GetMapping("/auth/schedule")
    public ResponseEntity<List<ScheduleDTO>> getScheduleRules(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting user schedule rules: %s", user.getUsername()));
        List<Schedule> rules = scheduleApplication.listSchedule(user.getId(), page, size, sort, asc);
        logger.info(String.format("Found %s schedule rules", rules.size()));
        return ResponseEntity.ok(rules.stream().map(ScheduleMapper::toScheduleDTO).collect(Collectors.toList()));
    }

    @GetMapping("/auth/work")
    public ResponseEntity<List<WorkDTO>> getWork(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting user work: %s", user.getUsername()));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Work> workList = scheduleApplication.listWork(user.getId(), startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s work", workList.size()));
        return ResponseEntity.ok(workList.stream().map(WorkMapper::toWorkDTO).collect(Collectors.toList()));
    }

    @GetMapping("/auth/pto")
    public ResponseEntity<List<PtoDTO>> getPto(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting user PTO: %s", user.getUsername()));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.plusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Pto> ptoList = scheduleApplication.listTimeOff(user.getId(), startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s PTO", ptoList.size()));
        return ResponseEntity.ok(ptoList.stream().map(PtoMapper::toPtoDTO).collect(Collectors.toList()));
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<ScheduleDTO> getScheduleRule(@PathVariable Integer scheduleId) {
        logger.info(String.format("Getting schedule rule with id=%s", scheduleId));
        Schedule schedule = scheduleApplication.getSchedule(scheduleId);
        logger.info(String.format("Schedule rule found with id=%s", schedule.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(ScheduleMapper.toScheduleDTO(schedule));
    }

    @GetMapping("/work/{workId}")
    public ResponseEntity<WorkDTO> getWork(@PathVariable Integer workId) {
        logger.info(String.format("Getting work with id=%s", workId));
        Work work = scheduleApplication.getWork(workId);
        logger.info(String.format("Work found with id=%s", work.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(WorkMapper.toWorkDTO(work));
    }

    @GetMapping("/pto/{ptoId}")
    public ResponseEntity<PtoDTO> getPto(@PathVariable Integer ptoId) {
        logger.info(String.format("Getting PTO with id=%s", ptoId));
        Pto pto = scheduleApplication.getPto(ptoId);
        logger.info(String.format("PTO found with id=%s", pto.getId()));
        return ResponseEntity.status(HttpStatus.OK).body(PtoMapper.toPtoDTO(pto));
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleDTO>> listScheduleRules(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing schedule rules with page=%s, size=%s, sort=%s, asc=%s", page, size, sort, asc));
        List<Schedule> rules = scheduleApplication.listSchedule(page, size, sort, asc);
        logger.info(String.format("Found %s schedule rules", rules.size()));
        return ResponseEntity.status(HttpStatus.OK).body(rules.stream().map(ScheduleMapper::toScheduleDTO).collect(Collectors.toList()));
    }

    @PostMapping("/users/{userId}/schedule")
    public ResponseEntity<ScheduleDTO> addNewScheduleToUser(
        @PathVariable Integer userId,
        @RequestBody ScheduleRequest request
    ) {
        logger.info(String.format("Adding new schedule to user with userId=%s, start=%s, end=%s, dayOfWeek=%s", userId, request.getStart(), request.getEnd(), request.getDayOfWeek()));
        Schedule schedule = scheduleApplication.addNewScheduleToUser(userId, request.getStart(), request.getEnd(), request.getDayOfWeek());
        ScheduleDTO scheduleDTO = ScheduleMapper.toScheduleDTO(schedule);
        logger.info(String.format("Added new schedule to user with userId=%s, start=%s, end=%s, dayOfWeek=%s", userId, request.getStart(), request.getEnd(), request.getDayOfWeek()));
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleDTO);
    }

    @DeleteMapping("/users/{userId}/schedule/{ruleId}")
    public ResponseEntity<Void> removeScheduleFromUser(
        @PathVariable Integer userId,
        @PathVariable Integer ruleId
    ) {
        logger.info(String.format("Removing schedule from user with userId=%s, ruleId=%s", userId, ruleId));
        scheduleApplication.removeScheduleFromUser(userId, ruleId);
        logger.info(String.format("Removed schedule from user with userId=%s, ruleId=%s", userId, ruleId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}/pto/{ptoId}")
    public ResponseEntity<Void> removePtoFromUser(
       @PathVariable Integer userId,
       @PathVariable Integer ptoId
    ) {
        logger.info(String.format("Removing PTO from user with userId=%s, ptoId=%s", userId, ptoId));
        scheduleApplication.removeTimeOffFromUser(userId, ptoId);
        logger.info(String.format("Removed PTO from user with userId=%s, ptoId=%s", userId, ptoId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pto")
    public ResponseEntity<List<PtoDTO>> listPto(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing PTO with start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", start, end, page, size, sort, asc));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Pto> ptoList = scheduleApplication.listTimeOff(startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s PTO", ptoList.size()));
        return ResponseEntity.status(HttpStatus.OK).body(ptoList.stream().map(PtoMapper::toPtoDTO).collect(Collectors.toList()));
    }

    @PostMapping("/users/{userId}/pto")
    public ResponseEntity<PtoDTO> addPtoToUser(
        @PathVariable Integer userId,
        @RequestBody PtoRequest request
    ) {
        logger.info(String.format("Adding PTO to user with userId=%s, day=%s", userId, request.getDay()));
        Pto pto = scheduleApplication.addNewTimeOffToUser(userId, request.getDay(), request.getType());
        PtoDTO ptoDTO = PtoMapper.toPtoDTO(pto);
        logger.info(String.format("Added PTO to user with userId=%s, day=%s", userId, request.getDay()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ptoDTO);
    }

    @PostMapping("/work/checkin")
    public ResponseEntity<WorkDTO> checkIn() {
        User user = getAuthenticatedUser();
        logger.info(String.format("Checking in user with userId=%s", user.getId()));
        Work work = scheduleApplication.checkIn(user.getId());
        WorkDTO workDTO = WorkMapper.toWorkDTO(work);
        logger.info(String.format("Checked in user with userId=%s", user.getId()));
        return ResponseEntity.ok(workDTO);
    }

    @PostMapping("/work/checkout")
    public ResponseEntity<WorkDTO> checkOut() {
        User user = getAuthenticatedUser();
        logger.info(String.format("Checking out user with userId=%s", user.getId()));
        Work work = scheduleApplication.checkOut(user.getId());
        WorkDTO workDTO = WorkMapper.toWorkDTO(work);
        logger.info(String.format("Checked out user with userId=%s", user.getId()));
        return ResponseEntity.ok(workDTO);
    }

    @GetMapping("/work")
    public ResponseEntity<List<WorkDTO>> listWork(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing work with start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", start, end, page, size, sort, asc));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Work> workList = scheduleApplication.listWork(startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s work", workList.size()));
        return ResponseEntity.status(HttpStatus.OK).body(workList.stream().map(WorkMapper::toWorkDTO).collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}/schedule")
    public ResponseEntity<List<ScheduleDTO>> listScheduleRulesByUser(
        @PathVariable Integer userId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing schedule rules by user with userId=%s, page=%s, size=%s, sort=%s, asc=%s", userId, page, size, sort, asc));
        List<Schedule> rules = scheduleApplication.listSchedule(userId, page, size, sort, asc);
        logger.info(String.format("Found %s schedule rules by user with userId=%s", rules.size(), userId));
        return ResponseEntity.status(HttpStatus.OK).body(rules.stream().map(ScheduleMapper::toScheduleDTO).collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}/work")
    public ResponseEntity<List<WorkDTO>> listWorkByUser(
        @PathVariable Integer userId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing work by user with userId=%s, start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", userId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Work> workList = scheduleApplication.listWork(userId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s work by user with userId=%s", workList.size(), userId));
        return ResponseEntity.status(HttpStatus.OK).body(workList.stream().map(WorkMapper::toWorkDTO).collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}/pto")
    public ResponseEntity<List<PtoDTO>> listPtoByUser(
        @PathVariable Integer userId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing PTO by user with userId=%s, start=%s, end=%s, page=%s, size=%s, sort=%s, asc=%s", userId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.plusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Pto> ptoList = scheduleApplication.listTimeOff(userId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s PTO by user with userId=%s", ptoList.size(), userId));
        return ResponseEntity.status(HttpStatus.OK).body(ptoList.stream().map(PtoMapper::toPtoDTO).collect(Collectors.toList()));
    }
}

class ScheduleRequest {
    private Time start;
    private Time end;
    private DayOfWeek dayOfWeek;

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
    }
}

class PtoRequest {
    private LocalDate day;
    private Pto.Type type;

    public Pto.Type getType() {
        return type;
    }

    public void setType(Pto.Type type) {
        this.type = type;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}