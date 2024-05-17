package com.martincastroalvarez.hex.hex.domain.models;

public class Sale extends Transaction {
    private User salesman;
    private Double price;

    public User getSalesman() {
        return salesman;
    }

    public void setSalesman(User salesman) {
        this.salesman = salesman;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getSales().add(this);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
