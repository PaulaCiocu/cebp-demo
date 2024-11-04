package com.stock_exchange.demo.repository;

import com.stock_exchange.demo.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
