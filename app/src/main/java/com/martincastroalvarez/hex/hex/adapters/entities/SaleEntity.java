package com.martincastroalvarez.hex.hex.adapters.entities;

import jakarta.annotation.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sales")
public class SaleEntity extends TransactionEntity {
    @Nonnull
    @Column(name = "price")
    private BigDecimal price;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "salesman_id", nullable = false)
    private UserEntity salesman;

    public UserEntity getSalesman() {
        return salesman;
    }

    public void setSalesman(UserEntity salesman) {
        this.salesman = salesman;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
