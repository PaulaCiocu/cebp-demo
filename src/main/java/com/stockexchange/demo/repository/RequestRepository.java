package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByBuyerId(Long buyerId);
}
