package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Meeting;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MeetingRepository {
    Optional<Meeting> get(Integer id);
    Page<Meeting> get(Pageable pageable);
    Page<Meeting> get(User user, Pageable pageable);
    Page<Meeting> get(User user, LocalDateTime start, Pageable pageable);
    Page<Meeting> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Meeting> get(LocalDateTime start, Pageable pageable);
    Page<Meeting> get(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Meeting save(Meeting meeting);
    void delete(Integer meetingId);
}