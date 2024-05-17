package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository {
    Optional<User> get(Integer Id);
    Optional<User> get(String email);
    Page<User> get(Pageable pageable);
    Page<User> get(User.Role role, Pageable pageable);
    Page<User> get(String query, Pageable pageable);
    User save(User user);
}