package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Purchase;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> get(Integer id);
    Page<Purchase> get(Pageable pageable);
    Page<Purchase> get(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Purchase> get(User provider, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Purchase> get(User provider, Pageable pageable);
    Page<Purchase> get(Product product, Pageable pageable);
    Page<Purchase> get(Product product, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Purchase save(Purchase purchase);
}