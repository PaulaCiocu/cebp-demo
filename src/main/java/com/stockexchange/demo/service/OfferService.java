package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Offer.OfferCreateDto;
import com.stockexchange.demo.dto.Offer.OfferUpdateDto;
import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.repository.BuyerRepository;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.SellerRepository;
import com.stockexchange.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;
    private final StockRepository stockRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, BuyerRepository buyerRepository, SellerRepository sellerRepository, StockRepository stockRepository) {
        this.offerRepository = offerRepository;
        this.buyerRepository = buyerRepository;
        this.sellerRepository = sellerRepository;
        this.stockRepository = stockRepository;

    }

    public Offer createOffer(OfferCreateDto offer) {

        Offer newOffer = new Offer();

        Buyer buyer = buyerRepository.findOneById(offer.getBuyerId());

        Seller seller = sellerRepository.findOneById(offer.getSellerId());

        Stock stock = stockRepository.findOneById(offer.getStockId());

        newOffer.setQuantity(offer.getQuantity());
        newOffer.setPricePerShare(offer.getPricePerShare());
        newOffer.setBuyer(buyer);
        newOffer.setSeller(seller);
        newOffer.setStock(stock);
        return offerRepository.save(newOffer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    public Offer updateOffer(Long id, OfferUpdateDto offerDetails) {
        Offer existingOffer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));



        if (offerDetails.getPricePerShare() != null) {
            existingOffer.setPricePerShare(offerDetails.getPricePerShare());
        }

        if (offerDetails.getQuantity() != null) {
            existingOffer.setPricePerShare(offerDetails.getPricePerShare());
        }

        if (offerDetails.getBuyerId() != null) {
            Buyer newBuyer = buyerRepository.findOneById(offerDetails.getBuyerId());
            existingOffer.setBuyer(newBuyer);
        }

        if (offerDetails.getSellerId() != null) {
            Seller newSeller = sellerRepository.findOneById(offerDetails.getSellerId());
            existingOffer.setSeller(newSeller);
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
