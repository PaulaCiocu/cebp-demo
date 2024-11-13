package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public Buyer createBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }

    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    public Optional<Buyer> getBuyerById(Long id) {
        return buyerRepository.findById(id);
    }

    public Buyer updateBuyer(Long id, Buyer buyerDetails) {
        return buyerRepository.findById(id)
                .map(buyer -> {
                    buyer.setName(buyerDetails.getName());
                    return buyerRepository.save(buyer);
                })
                .orElseThrow(() -> new RuntimeException("Buyer not found with id " + id));
    }

    public void deleteBuyer(Long id) {
        buyerRepository.deleteById(id);
    }
}
