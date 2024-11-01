package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final OfferRepository offerRepository;

    public SellerService(SellerRepository sellerRepository, OfferRepository offerRepository) {
        this.sellerRepository = sellerRepository;
        this.offerRepository = offerRepository;
    }

    public Seller addSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seller with ID " + id + " not found."));
    }

    public List<Offer> getOffersBySeller(Long sellerId) {
        return offerRepository.findBySellerId(sellerId);
    }
}
