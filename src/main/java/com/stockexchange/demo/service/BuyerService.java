package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    public Buyer getBuyerById(Long id) {
        return buyerRepository.findById(id).orElse(null);
    }

    public Buyer addBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }
}
