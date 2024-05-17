package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.ProductEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.PurchaseEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.PurchaseMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.ProductMapper;
import com.martincastroalvarez.hex.hex.domain.models.Purchase;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.PurchaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaPurchaseRepository extends JpaRepository<PurchaseEntity, Integer>, PurchaseRepository {
    @Override
    default Optional<Purchase> get(Integer id) {
        return findById(id).map(PurchaseMapper::toPurchase);
    }

    @Override
    default Page<Purchase> get(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return find(start, end, pageable).map(PurchaseMapper::toPurchase);
    }

    @Override
    default Page<Purchase> get(Pageable pageable) {
        return find(pageable).map(PurchaseMapper::toPurchase);
    }

    @Override
    @Query("SELECT p FROM PurchaseEntity p WHERE p.provider = :user AND p.datetime >= :start AND p.datetime <= :end")
    default Page<Purchase> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByUserAndDatetimeRange(UserMapper.toUserEntity(user), start, end, pageable).map(PurchaseMapper::toPurchase);
    }

    @Override
    default Page<Purchase> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(PurchaseMapper::toPurchase);
    }

    @Override
    @Query("SELECT p FROM PurchaseEntity p WHERE p.product = :product AND p.datetime >= :start AND p.datetime <= :end")
    default Page<Purchase> get(Product product, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByProductAndDatetimeRange(ProductMapper.toProductEntity(product), start, end, pageable).map(PurchaseMapper::toPurchase);
    }

    @Override
    @Query("SELECT p FROM PurchaseEntity p WHERE p.product = :product")
    default Page<Purchase> get(Product product, Pageable pageable) {
        return findByProduct(ProductMapper.toProductEntity(product), pageable).map(PurchaseMapper::toPurchase);
    }

    @Override
    default Purchase save(Purchase purchase) {
        return PurchaseMapper.toPurchase(save(PurchaseMapper.toPurchaseEntity(purchase)));
    }

    @Query("SELECT p FROM PurchaseEntity p WHERE p.provider = :user AND p.datetime >= :start AND p.datetime <= :end")
    Page<PurchaseEntity> findByUserAndDatetimeRange(UserEntity user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT p FROM PurchaseEntity p WHERE p.provider = :user")
    Page<PurchaseEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT p FROM PurchaseEntity p WHERE p.datetime >= :start AND p.datetime <= :end")
    Page<PurchaseEntity> find(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT p FROM PurchaseEntity p")
    Page<PurchaseEntity> find(Pageable pageable);

    @Query("SELECT p FROM PurchaseEntity p WHERE p.product = :product AND p.datetime >= :start AND p.datetime <= :end")
    Page<PurchaseEntity> findByProductAndDatetimeRange(ProductEntity product, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT p FROM PurchaseEntity p WHERE p.product = :product")
    Page<PurchaseEntity> findByProduct(ProductEntity product, Pageable pageable);
}
