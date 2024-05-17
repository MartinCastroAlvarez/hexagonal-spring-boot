package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Sale;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SaleRepository {
    Optional<Sale> get(Integer id);
    Page<Sale> get(Pageable pageable);
    Page<Sale> get(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Sale> get(User salesman, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Sale> get(User salesman, Pageable pageable);
    Page<Sale> get(Product product, Pageable pageable);
    Page<Sale> get(Product product, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Sale save(Sale sale);
}