package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Message;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MessageRepository {
    Optional<Message> get(Integer id);
    Page<Message> get(Pageable pageable);
    Page<Message> get(User user, LocalDateTime start, Pageable pageable);
    Page<Message> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Message> get(User user, Pageable pageable);
    Page<Message> get(LocalDateTime start, Pageable pageable);
    Page<Message> get(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Message save(Message message);
    void delete(Integer messageId);
}