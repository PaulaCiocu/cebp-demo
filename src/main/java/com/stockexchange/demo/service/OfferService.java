package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Offer.OfferDto;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.StockRepository;
import com.stockexchange.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, UserRepository userRepository, StockRepository stockRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;

    }

    public Offer createOffer(OfferDto offer) {

        Offer newOffer = new Offer();

        User seller = userRepository.findOneById(offer.getSellerId());
        Stock stock = stockRepository.findOneById(offer.getStockId());

        newOffer.setQuantity(offer.getQuantity());
        newOffer.setPricePerShare(offer.getPricePerShare());
        newOffer.setUser(seller);
        newOffer.setStock(stock);
        return offerRepository.save(newOffer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    public Offer updateOffer(Long id, OfferDto offerDetails) {
        Offer existingOffer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));



        if (offerDetails.getPricePerShare() != null) {
            existingOffer.setPricePerShare(offerDetails.getPricePerShare());
        }

        if (offerDetails.getQuantity() != null) {
            existingOffer.setPricePerShare(offerDetails.getPricePerShare());
        }

        if (offerDetails.getSellerId() != null) {
            User newSeller = userRepository.findOneById(offerDetails.getSellerId());
            existingOffer.setUser(newSeller);
        }

        if (offerDetails.getStockId() != null) {
            Stock newStock = stockRepository.findOneById(offerDetails.getStockId());
            existingOffer.setStock(newStock);
        }

        return offerRepository.save(existingOffer);
    }

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
