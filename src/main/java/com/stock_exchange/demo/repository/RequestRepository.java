package com.stock_exchange.demo.repository;

import com.stock_exchange.demo.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByBuyerId(String buyerId);
}
