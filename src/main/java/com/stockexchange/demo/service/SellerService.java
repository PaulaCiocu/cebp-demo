package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Seller updateSeller(Long id, Seller sellerDetails) {
        return sellerRepository.findById(id)
                .map(seller -> {
                    seller.setName(sellerDetails.getName());
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Seller not found with id " + id));
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}
