package com.stock_exchange.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Offer> offers;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Request> requests;
}
