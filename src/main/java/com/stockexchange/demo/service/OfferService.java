package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    public Offer updateOffer(Long id, Offer offerDetails) {
        return offerRepository.findById(id)
                .map(offer -> {
                    offer.setStock(offerDetails.getStock());
                    offer.setPricePerShare(offerDetails.getPricePerShare());
                    offer.setQuantity(offerDetails.getQuantity());
                    return offerRepository.save(offer);
                })
                .orElseThrow(() -> new RuntimeException("Offer not found with id " + id));
    }

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
