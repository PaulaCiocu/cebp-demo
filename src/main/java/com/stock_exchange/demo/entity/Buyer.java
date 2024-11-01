package com.stock_exchange.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String buyerId;
    private String name;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Offer> offers;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Request> requests;

    // Constructors, getters, and setters
    public Buyer() {}

    public Buyer(String buyerId, String name) {
        this.buyerId = buyerId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
