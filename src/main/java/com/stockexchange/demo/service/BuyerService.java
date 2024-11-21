package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Buyer.BuyerCreateDto;
import com.stockexchange.demo.dto.Buyer.BuyerUpdateDto;
import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public Buyer createBuyer(BuyerCreateDto buyer) {
        Buyer newBuyer = new Buyer();
        newBuyer.setName(buyer.getName());
        return buyerRepository.save(newBuyer);
    }

    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    public Optional<Buyer> getBuyerById(Long id) {
        return buyerRepository.findById(id);
    }

    public Buyer updateBuyer(Long id, BuyerUpdateDto buyerDetails) {

        Buyer existingBuyer = buyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (buyerDetails.getName() != null) {
            existingBuyer.setName(buyerDetails.getName());
        }

        return buyerRepository.save(existingBuyer);
    }

    public void deleteBuyer(Long id) {
        buyerRepository.deleteById(id);
    }
}
