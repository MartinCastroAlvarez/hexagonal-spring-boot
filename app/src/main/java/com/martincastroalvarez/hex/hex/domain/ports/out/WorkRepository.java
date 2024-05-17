package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Work;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkRepository {
    Optional<Work> get(Integer id);
    Page<Work> get(User user, Pageable pageable);
    Page<Work> get(Pageable pageable);
    Page<Work> get(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Work> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Work save(Work work);
}