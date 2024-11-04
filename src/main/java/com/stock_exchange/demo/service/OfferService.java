package com.stock_exchange.demo.service;

import com.stock_exchange.demo.entity.Offer;
import com.stock_exchange.demo.entity.Seller;
import com.stock_exchange.demo.entity.Stock;
import com.stock_exchange.demo.repository.OfferRepository;
import com.stock_exchange.demo.repository.SellerRepository;
import com.stock_exchange.demo.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final StockRepository stockRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository,
                        StockRepository stockRepository,
                        SellerRepository sellerRepository) {
        this.offerRepository = offerRepository;
        this.stockRepository = stockRepository;
        this.sellerRepository = sellerRepository;
    }

    public Offer createOffer(Offer offer) {
        // Validate that the Stock exists
        Stock stock = stockRepository.findById(offer.getStock().getId())
                .orElseThrow(() -> new EntityNotFoundException("Stock not found"));

        // Validate that the Seller exists
        Seller seller = sellerRepository.findById(offer.getSeller().getId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        // Set the validated Stock and Seller to the Offer
        offer.setStock(stock);
        offer.setSeller(seller);

        // Save and return the Offer
        return offerRepository.save(offer);
    }
}
