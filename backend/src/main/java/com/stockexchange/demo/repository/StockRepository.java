package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findOneById(Long id);
}