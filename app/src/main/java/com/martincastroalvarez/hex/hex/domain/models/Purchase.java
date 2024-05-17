package com.martincastroalvarez.hex.hex.domain.models;

public class Purchase extends Transaction {
    private User provider;
    private Double cost;

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getPurchases().add(this);
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
