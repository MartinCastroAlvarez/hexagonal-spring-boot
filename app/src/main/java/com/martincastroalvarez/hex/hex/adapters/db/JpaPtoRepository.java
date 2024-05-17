package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.PtoEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.PtoMapper;
import com.martincastroalvarez.hex.hex.domain.models.Pto;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.PtoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface JpaPtoRepository extends JpaRepository<PtoEntity, Integer>, PtoRepository {
    @Override
    default Optional<Pto> get(Integer id) {
        return findById(id).map(PtoMapper::toPto);
    }

    @Override
    @Query("SELECT p FROM PtoEntity p")
    default Page<Pto> get(Pageable pageable) {
        return find(pageable).map(PtoMapper::toPto);
    }

    @Override
    @Query("SELECT p FROM PtoEntity p WHERE p.day >= :start AND p.day <= :end")
    default Page<Pto> get(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return find(start.toLocalDate(), end.toLocalDate(), pageable).map(PtoMapper::toPto);
    }

    @Override
    default Page<Pto> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(PtoMapper::toPto);
    }

    @Override
    @Query("SELECT p FROM PtoEntity p WHERE p.user = :user AND p.day >= :start AND p.day <= :end")
    default Page<Pto> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByUserAndDateRange(UserMapper.toUserEntity(user), start.toLocalDate(), end.toLocalDate(), pageable).map(PtoMapper::toPto);
    }

    @Override
    default Pto save(Pto pto) {
        return PtoMapper.toPto(save(PtoMapper.toPtoEntity(pto)));
    }

    @Query("SELECT p FROM PtoEntity p WHERE p.user = :user AND p.day >= :start AND p.day <= :end")
    Page<PtoEntity> findByUserAndDateRange(UserEntity user, LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT p FROM PtoEntity p WHERE p.user = :user")
    Page<PtoEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT p FROM PtoEntity p WHERE p.day >= :start AND p.day <= :end")
    Page<PtoEntity> find(LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT p FROM PtoEntity p")
    Page<PtoEntity> find(Pageable pageable);

    @Override
    default void delete(Integer ptoId) {
        deleteById(ptoId);
    }
}
