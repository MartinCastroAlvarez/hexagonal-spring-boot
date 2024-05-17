package com.martincastroalvarez.hex.hex.adapters.entities;

import java.time.LocalDateTime;
import jakarta.annotation.*;
import jakarta.persistence.*;

@Entity
@Table(name = "work")
public class WorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_generator")
    @SequenceGenerator(name="worker_generator", sequenceName = "worker_id_seq", allocationSize=1)
    private Integer id;

    @Nonnull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Nullable
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
