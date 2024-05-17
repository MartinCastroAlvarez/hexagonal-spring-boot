package com.martincastroalvarez.hex.hex.adapters.entities;

import com.martincastroalvarez.hex.hex.domain.models.Pto;
import jakarta.persistence.*;
import jakarta.annotation.*;
import java.time.LocalDate;

@Entity
@Table(name = "ptos")
public class PtoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pto_generator")
    @SequenceGenerator(name="pto_id_seq", sequenceName = "pto_id_seq", allocationSize=1)
    private Integer id;

    @Nonnull
    @Column(name = "day", nullable = false)
    private LocalDate day;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Pto.Type type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Pto.Type getType() {
        return type;
    }

    public void setType(Pto.Type type) {
        this.type = type;
    }
}
