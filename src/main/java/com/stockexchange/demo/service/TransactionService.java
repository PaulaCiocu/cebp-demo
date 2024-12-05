package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.StockRepository;
import com.stockexchange.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private StockRepository stockRepository;
    private OfferService offerService;
    private RequestService requestService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, OfferService offerService, RequestService requestService) {
        this.transactionRepository = transactionRepository;
        this.offerService = offerService;
        this.requestService = requestService;
    }

    public ResponseEntity<Transaction> createTransaction(Long offerId, Long requestId) {

        Offer offer = offerService.getOfferById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found with ID: " + offerId));
        Request request = requestService.getRequestById(requestId)
               .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + requestId));

        int quantity = Math.min(offer.getQuantity(), request.getQuantity());
        double pricePerShare = offer.getPricePerShare();

        Transaction transaction = Transaction.builder()
                .offer(offer)
                .request(request)
                .quantity(quantity)
                .pricePerShare(pricePerShare)
                .build();

        offer.setQuantity(offer.getQuantity() - quantity);
        request.setQuantity(request.getQuantity() - quantity);

        if (offer.getQuantity() == 0) {
            offer.setIsFulfilled(true);
        }
        if (request.getQuantity() == 0) {
            request.setIsFulfilled(true);
        }

        Stock stock = stockRepository.findOneById(offer.getStock().getId());
        stock.setRemainingShares(stock.getRemainingShares() - quantity);
        stockRepository.save(stock);

        transactionRepository.save(transaction);
        return ResponseEntity.ok(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getTransactionsByOfferId(Long offerId) {
        Optional<Offer> offerOptional = offerService.getOfferById(offerId);

        if (offerOptional.isEmpty()) {
            throw new IllegalArgumentException("Offer not found for ID: " + offerId);
        }

        Offer offer = offerOptional.get();
        List<Transaction> transaction = transactionRepository.findByOffer(offer);

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found for Offer ID: " + offerId);
        }

        return transaction;
    }

    public List<Transaction> getTransactionsByRequestId(Long requestId) {
        Optional<Request> requestOptional = requestService.getRequestById(requestId);

        if (requestOptional.isEmpty()) {
            throw new IllegalArgumentException("Offer not found for ID: " + requestId);
        }

        Request request = requestOptional.get();
        List<Transaction> transaction = transactionRepository.findByRequest(request);

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found for Offer ID: " + requestId);
        }

        return transaction;
    }


    public Set<Transaction> getTransactionByUserId(Long userId) {
        // Retrieve offers and requests
        Optional<List<Offer>> offerList = offerService.getOfferByUserId(userId);
        Optional<List<Request>> requestList = requestService.getRequestByUserId(userId);

        Set<Transaction> transactionList = new HashSet<>();

        // Process offers
        if (offerList.isPresent()) {
            for (Offer offer : offerList.get()) {
                System.out.println("Processing offer: " + offer);
                List<Transaction> transactions = getTransactionsByOfferId(offer.getId());
                transactionList.addAll(transactions);
            }
        } else {
            System.out.println("No offers found for userId: " + userId);
        }

        // Process requests
        if (requestList.isPresent()) {
            for (Request request : requestList.get()) {
                System.out.println("Processing request: " + request);
                List<Transaction> transactions = getTransactionsByRequestId(request.getId());
                transactionList.addAll(transactions);
            }
        } else {
            System.out.println("No requests found for userId: " + userId);
        }

        return transactionList;
    }

}
