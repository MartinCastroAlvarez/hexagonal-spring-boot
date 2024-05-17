package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.ScheduleEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.ScheduleMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.Schedule;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaScheduleRepository extends JpaRepository<ScheduleEntity, Integer>, ScheduleRepository {
    @Override
    default Optional<Schedule> get(Integer id) {
        return findById(id).map(ScheduleMapper::toSchedule);
    }

    @Override
    @Query("SELECT s FROM ScheduleEntity s")
    default Page<Schedule> get(Pageable pageable) {
        return find(pageable).map(ScheduleMapper::toSchedule);
    }

    @Override
    @Query("SELECT s FROM ScheduleEntity s WHERE s.user = :user")
    default Page<Schedule> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(ScheduleMapper::toSchedule);
    }

    @Override
    default Schedule save(Schedule schedule) {
        return ScheduleMapper.toSchedule(save(ScheduleMapper.toScheduleEntity(schedule)));
    }

    @Query("SELECT s FROM ScheduleEntity s WHERE s.user = :user")
    Page<ScheduleEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT s FROM ScheduleEntity s")
    Page<ScheduleEntity> find(Pageable pageable);

    @Override
    default void delete(Integer scheduleId) {
        deleteById(scheduleId);
    }
}
