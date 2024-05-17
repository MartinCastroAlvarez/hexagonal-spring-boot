package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Schedule;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> get(Integer id);
    Page<Schedule> get(Pageable pageable);
    Page<Schedule> get(User user, Pageable pageable);
    Schedule save(Schedule meeting);
    void delete(Integer scheduleId);
}