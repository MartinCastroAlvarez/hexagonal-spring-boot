package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Pto;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PtoRepository {
    Optional<Pto> get(Integer id);
    Page<Pto> get(Pageable pageable);
    Page<Pto> get(User user, Pageable pageable);
    Page<Pto> get(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Pto> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Pto save(Pto pto);
    void delete(Integer ptoId);
}