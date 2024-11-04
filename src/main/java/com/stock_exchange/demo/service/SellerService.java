package com.stock_exchange.demo.service;

import com.stock_exchange.demo.entity.Seller;
import com.stock_exchange.demo.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }
}
