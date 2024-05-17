package com.martincastroalvarez.hex.hex.domain.ports.out;

import com.martincastroalvarez.hex.hex.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> get(Integer Id);
    Optional<Product> get(String name);
    Page<Product> get(Pageable pageable);
    Page<Product> get(String query, Pageable pageable);
    Product save(Product product);
}