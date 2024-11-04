package com.stock_exchange.demo.entity;

import jakarta.persistence.*;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    private int quantity;
    private double pricePerShare;

    public Offer() {}

    public Offer(Stock stock, Seller seller, int quantity, double pricePerShare) {
        this.stock = stock;
        this.seller = seller;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
    }

    public Long getId() {
        return id;
    }

    public Stock getStock() {
        return stock;
    }

    public Seller getSeller() {
        return seller;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFulfilled() {
        return quantity <= 0;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", stock=" + stock +
                ", seller=" + seller +
                ", quantity=" + quantity +
                ", pricePerShare=" + pricePerShare +
                '}';
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}