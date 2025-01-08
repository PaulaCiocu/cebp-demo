package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Offer.OfferDto;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.StockRepository;
import com.stockexchange.demo.repository.TransactionRepository;
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
    private final TransactionRepository transactionRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, UserRepository userRepository, StockRepository stockRepository, TransactionRepository transactionRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;

    }

    public Offer createOffer(OfferDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stock stock = stockRepository.findById(dto.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Transaction sourceTx = transactionRepository.findById(dto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        User transactionOfferUser = sourceTx.getOffer().getUser();
        User transactionRequestUser = sourceTx.getRequest().getUser();
        if (!transactionOfferUser.getId().equals(user.getId())
                && !transactionRequestUser.getId().equals(user.getId())) {
            throw new RuntimeException("User does not own that transaction");
        }

        if (sourceTx.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough shares in that transaction to create this offer");
        }

        sourceTx.setQuantity(sourceTx.getQuantity() - dto.getQuantity());
        transactionRepository.save(sourceTx);

        Offer newOffer = new Offer();
        newOffer.setQuantity(dto.getQuantity());
        newOffer.setPricePerShare(dto.getPricePerShare());
        newOffer.setStock(stock);
        newOffer.setUser(user);
        newOffer.setIsFulfilled(false);

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

        if (offerDetails.getUserId() != null) {
            User newSeller = userRepository.findOneById(offerDetails.getUserId());
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

    public Optional<List<Offer>> getOfferByUserId(Long userId) {
        List<Offer> offers = offerRepository.findByUserId(userId);
        return offers.isEmpty() ? Optional.empty() : Optional.of(offers);
    }

    public List<Offer> getAllOffersByStockId(Long id) {
        List<Offer> offers = offerRepository.findByStockId(id);
        return offers;
    }
}
