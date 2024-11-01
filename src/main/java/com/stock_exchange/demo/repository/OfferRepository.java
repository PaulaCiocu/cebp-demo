package com.stock_exchange.demo.repository;

import com.stock_exchange.demo.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findBySellerId(String sellerId);
}
