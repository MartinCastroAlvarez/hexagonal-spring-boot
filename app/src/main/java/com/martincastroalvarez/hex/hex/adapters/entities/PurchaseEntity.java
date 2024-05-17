package com.martincastroalvarez.hex.hex.adapters.entities;

import jakarta.annotation.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchases")
public class PurchaseEntity extends TransactionEntity {
    @Nonnull
    @Column(name = "cost")
    private BigDecimal cost;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private UserEntity provider;

    public UserEntity getProvider() {
        return provider;
    }

    public void setProvider(UserEntity provider) {
        this.provider = provider;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
