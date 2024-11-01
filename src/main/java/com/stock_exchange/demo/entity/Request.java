package com.stock_exchange.demo.entity;

import jakarta.persistence.*;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    private int quantity;
    private double maxPricePerShare;
    private String buyerId;

    public Request() {}

    public Request(Stock stock, int quantity, double maxPricePerShare, String buyerId) {
        this.stock = stock;
        this.quantity = quantity;
        this.maxPricePerShare = maxPricePerShare;
        this.buyerId = buyerId;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getMaxPricePerShare() {
        return maxPricePerShare;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isFulfilled() {
        return quantity <= 0;
    }

    @Override
    public String toString() {
        return "Request{" +
                "stock=" + stock.getCompanyName() +
                ", quantity=" + quantity +
                ", maxPricePerShare=" + maxPricePerShare +
                ", buyerId='" + buyerId + '\'' +
                '}';
    }
}
