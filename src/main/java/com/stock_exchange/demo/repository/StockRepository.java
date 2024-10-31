package com.stock_exchange.demo.repository;

import com.stock_exchange.demo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}