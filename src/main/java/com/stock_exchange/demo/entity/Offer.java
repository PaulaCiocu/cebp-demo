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

    private int quantity;
    private double pricePerShare;
    private String sellerId;

    public Offer() {}

    public Offer(Stock stock, int quantity, double pricePerShare, String sellerId) {
        this.stock = stock;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.sellerId = sellerId;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public String getSellerId() {
        return sellerId;
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
                "stock=" + stock.getCompanyName() +
                ", quantity=" + quantity +
                ", pricePerShare=" + pricePerShare +
                ", sellerId='" + sellerId + '\'' +
                '}';
    }
}
