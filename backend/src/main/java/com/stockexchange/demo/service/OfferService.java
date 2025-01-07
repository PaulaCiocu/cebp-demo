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
    private TransactionRepository transactionRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, UserRepository userRepository, StockRepository stockRepository, TransactionRepository transactionRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;

    }

    public Offer createOffer(OfferDto dto) {
        // 1) Lookup the user
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2) Lookup the stock
        Stock stock = stockRepository.findById(dto.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        // 3) Lookup the transaction we want to subtract from
        Transaction sourceTx = transactionRepository.findById(dto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // 4) Check if that transaction belongs to this user (to be safe)
        //    This depends on your domain logic. If the user is the 'offer' user in that transaction:
        User transactionOfferUser = sourceTx.getOffer().getUser();
        User transactionRequestUser = sourceTx.getRequest().getUser();
        if (!transactionOfferUser.getId().equals(user.getId())
                && !transactionRequestUser.getId().equals(user.getId())) {
            throw new RuntimeException("User does not own that transaction");
        }

        // 5) Check if the transaction quantity is >= the requested offer quantity
        if (sourceTx.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough shares in that transaction to create this offer");
        }

        // 6) Subtract
        sourceTx.setQuantity(sourceTx.getQuantity() - dto.getQuantity());
        transactionRepository.save(sourceTx);

        // 7) Create a new Offer record
        Offer newOffer = new Offer();
        newOffer.setQuantity(dto.getQuantity());
        newOffer.setPricePerShare(dto.getPricePerShare());
        newOffer.setStock(stock);
        newOffer.setUser(user);
        newOffer.setIsFulfilled(false);  // presumably default to false

        // 8) Save the new offer
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
