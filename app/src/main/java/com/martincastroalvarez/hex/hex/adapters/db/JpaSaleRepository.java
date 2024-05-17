package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.SaleEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.ProductEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.SaleMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.ProductMapper;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.Sale;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.SaleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaSaleRepository extends JpaRepository<SaleEntity, Integer>, SaleRepository {
    @Override
    default Optional<Sale> get(Integer id) {
        return findById(id).map(SaleMapper::toSale);
    }

    @Override
    default Page<Sale> get(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return find(start, end, pageable).map(SaleMapper::toSale);
    }

    @Override
    default Page<Sale> get(Pageable pageable) {
        return find(pageable).map(SaleMapper::toSale);
    }

    @Override
    @Query("SELECT s FROM SaleEntity s WHERE s.salesman = :user AND s.datetime >= :start AND s.datetime <= :end")
    default Page<Sale> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findBySalesmanAndDatetimeRange(UserMapper.toUserEntity(user), start, end, pageable).map(SaleMapper::toSale);
    }

    @Override
    default Page<Sale> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(SaleMapper::toSale);
    }

    @Override
    @Query("SELECT s FROM SaleEntity s WHERE s.product = :product AND s.datetime >= :start AND s.datetime <= :end")
    default Page<Sale> get(Product product, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByProductAndDatetimeRange(ProductMapper.toProductEntity(product), start, end, pageable).map(SaleMapper::toSale);
    }

    @Override
    @Query("SELECT s FROM SaleEntity s WHERE s.product = :product")
    default Page<Sale> get(Product product, Pageable pageable) {
        return findByProduct(ProductMapper.toProductEntity(product), pageable).map(SaleMapper::toSale);
    }

    @Override
    default Sale save(Sale sale) {
        return SaleMapper.toSale(save(SaleMapper.toSaleEntity(sale)));
    }

    @Query("SELECT s FROM SaleEntity s WHERE s.salesman = :user AND s.datetime >= :start AND s.datetime <= :end")
    Page<SaleEntity> findBySalesmanAndDatetimeRange(UserEntity user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT s FROM SaleEntity s WHERE s.salesman = :user")
    Page<SaleEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT s FROM SaleEntity s WHERE s.datetime >= :start AND s.datetime <= :end")
    Page<SaleEntity> find(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT s FROM SaleEntity s")
    Page<SaleEntity> find(Pageable pageable);

    @Query("SELECT s FROM SaleEntity s WHERE s.product = :product AND s.datetime >= :start AND s.datetime <= :end")
    Page<SaleEntity> findByProductAndDatetimeRange(ProductEntity product, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT s FROM SaleEntity s WHERE s.product = :product")
    Page<SaleEntity> findByProduct(ProductEntity product, Pageable pageable);
}
