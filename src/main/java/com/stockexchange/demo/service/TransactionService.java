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
import java.util.List;
import java.util.Optional;

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
}
