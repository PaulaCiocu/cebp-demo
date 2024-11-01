package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findBySellerId(Long sellerId);
}
