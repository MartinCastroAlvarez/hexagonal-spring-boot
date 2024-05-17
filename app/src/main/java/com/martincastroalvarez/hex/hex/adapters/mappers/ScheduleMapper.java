package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.ScheduleEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.ScheduleDTO;
import com.martincastroalvarez.hex.hex.domain.models.Schedule;
import java.sql.Time;

public class ScheduleMapper {
    public static Schedule toSchedule(ScheduleEntity scheduleEntity) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleEntity.getId());
        schedule.setStartTime(scheduleEntity.getStartTime());
        schedule.setEndTime(scheduleEntity.getEndTime()); 
        schedule.setDayOfWeek(scheduleEntity.getDayOfWeek());
        schedule.setUser(UserMapper.toUser(scheduleEntity.getUser()));
        return schedule;
    }

    public static ScheduleEntity toScheduleEntity(Schedule schedule) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(schedule.getId());
        scheduleEntity.setStartTime(Time.valueOf(schedule.getStartTime().toLocalTime()));
        scheduleEntity.setEndTime(Time.valueOf(schedule.getEndTime().toLocalTime()));
        scheduleEntity.setDayOfWeek(schedule.getDayOfWeek());
        scheduleEntity.setUser(UserMapper.toUserEntity(schedule.getUser()));
        return scheduleEntity;
    }

    public static ScheduleDTO toScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setStartTime(schedule.getStartTime());
        scheduleDTO.setEndTime(schedule.getEndTime());
        scheduleDTO.setDayOfWeek(schedule.getDayOfWeek());
        scheduleDTO.setUserId(schedule.getUser().getId());
        return scheduleDTO;
    }
}
