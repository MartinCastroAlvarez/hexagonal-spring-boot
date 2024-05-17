package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.ProductEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.ProductMapper;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.ports.out.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Integer>, ProductRepository {
    @Override
    default Optional<Product> get(Integer id) {
        return findById(id).map(ProductMapper::toProduct);
    }

    @Override
    default Optional<Product> get(String name) {
        return findByName(name).map(ProductMapper::toProduct);
    }

    @Override
    default Page<Product> get(Pageable pageable) {
        return findAll(pageable).map(ProductMapper::toProduct);
    }

    @Override
    default Page<Product> get(String query, Pageable pageable) {
        return findByNameLike(query, pageable).map(ProductMapper::toProduct);
    }

    @Override
    default Product save(Product product) {
        return ProductMapper.toProduct(save(ProductMapper.toProductEntity(product)));
    }

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<ProductEntity> findByName(String name);

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ProductEntity> findByNameLike(String query, Pageable pageable);
}
