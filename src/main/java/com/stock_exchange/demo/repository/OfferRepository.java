package com.stock_exchange.demo.repository;

import com.stock_exchange.demo.entity.Offer;
import com.stock_exchange.demo.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}