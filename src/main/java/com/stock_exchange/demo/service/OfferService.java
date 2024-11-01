package com.stock_exchange.demo.service;

import com.stock_exchange.demo.entity.Offer;
import com.stock_exchange.demo.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer addOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public List<Offer> getOffersBySeller(Long sellerId) {
        return offerRepository.findBySellerId(sellerId);
    }
}
