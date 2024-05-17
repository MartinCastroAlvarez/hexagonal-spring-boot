package com.martincastroalvarez.hex.hex.adapters.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.annotation.*;
import java.math.BigDecimal;

@MappedSuperclass
public abstract class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_generator")
    @SequenceGenerator(name="transaction_generator", sequenceName = "transaction_id_seq", allocationSize=1)
    private Integer id;
    
    @Nonnull
    @Column(name = "amount")
    private BigDecimal amount;
    
    @Nonnull
    @Column(name = "datetime")
    private LocalDateTime datetime;
    
    @Nonnull
    @ManyToOne
    @JoinColumn(name = "product_id")
    protected ProductEntity product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
