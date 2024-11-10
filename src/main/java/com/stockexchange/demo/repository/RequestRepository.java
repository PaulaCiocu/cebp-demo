package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByBuyerId(Long buyerId);
    List<Request> findByStockCompanyName(String stockCompanyName);
}
