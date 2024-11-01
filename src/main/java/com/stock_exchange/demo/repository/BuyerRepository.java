package com.stock_exchange.demo.repository;

import com.stock_exchange.demo.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    // Additional custom query methods can be added here if needed
}
