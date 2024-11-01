package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

}
