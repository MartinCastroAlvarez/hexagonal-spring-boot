package com.martincastroalvarez.hex.hex.adapters.db;

import com.martincastroalvarez.hex.hex.adapters.entities.UserEntity;
import com.martincastroalvarez.hex.hex.adapters.entities.WorkEntity;
import com.martincastroalvarez.hex.hex.adapters.mappers.WorkMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.UserMapper;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.models.Work;
import com.martincastroalvarez.hex.hex.domain.ports.out.WorkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaWorkRepository extends JpaRepository<WorkEntity, Integer>, WorkRepository {
    @Override
    default Optional<Work> get(Integer id) {
        return findById(id).map(WorkMapper::toWork);
    }

    @Override
    @Query("SELECT w FROM WorkEntity w")
    default Page<Work> get(Pageable pageable) {
        return find(pageable).map(WorkMapper::toWork);
    }

    @Override
    @Query("SELECT w FROM WorkEntity w WHERE w.startTime >= :start AND (w.endTime <= :end OR w.endTime IS NULL)")
    default Page<Work> get(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return find(start, end, pageable).map(WorkMapper::toWork);
    }

    @Override
    default Page<Work> get(User user, Pageable pageable) {
        return findByUser(UserMapper.toUserEntity(user), pageable).map(WorkMapper::toWork);
    }

    @Override
    @Query("SELECT w FROM WorkEntity w WHERE w.user = :user AND w.startTime >= :start AND (w.endTime <= :end OR w.endTime IS NULL)")
    default Page<Work> get(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return findByUserAndTimeRange(UserMapper.toUserEntity(user), start, end, pageable).map(WorkMapper::toWork);
    }

    @Override
    default Work save(Work work) {
        return WorkMapper.toWork(save(WorkMapper.toWorkEntity(work)));
    }

    @Query("SELECT w FROM WorkEntity w WHERE w.user = :user AND w.startTime >= :start AND (w.endTime <= :end OR w.endTime IS NULL)")
    Page<WorkEntity> findByUserAndTimeRange(UserEntity user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT w FROM WorkEntity w WHERE w.user = :user")
    Page<WorkEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT w FROM WorkEntity w WHERE w.startTime >= :start AND (w.endTime <= :end OR w.endTime IS NULL)")
    Page<WorkEntity> find(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT w FROM WorkEntity w")
    Page<WorkEntity> find(Pageable pageable);
}
