package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findBySellerId(Long sellerId);
    List<Offer> findByStockCompanyName(String stockCompanyName);
}
