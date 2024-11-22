package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.repository.StockRepository;
import com.stockexchange.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private StockRepository stockRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Offer offer, Request request) {
        int quantity = Math.min(offer.getQuantity(), request.getQuantity());
        double pricePerShare = offer.getPricePerShare();

        Transaction transaction = Transaction.builder()
                .offer(offer)
                .request(request)
                .quantity(quantity)
                .pricePerShare(pricePerShare)
                .build();

        // Update offer and request quantities
        offer.setQuantity(offer.getQuantity() - quantity);
        request.setQuantity(request.getQuantity() - quantity);

        // Fetch and update the stock related to the offer
        Stock stock = stockRepository.findOneById(offer.getStock().getId());
        stock.setRemainingShares(stock.getRemainingShares() - quantity);
        stockRepository.save(stock);

        transactionRepository.save(transaction);

        return transaction;
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
