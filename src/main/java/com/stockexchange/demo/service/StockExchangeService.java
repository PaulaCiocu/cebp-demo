package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.RequestRepository;
import com.stockexchange.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class StockExchangeService {

    private final OfferRepository offerRepository;
    private final RequestRepository requestRepository;
    private final TransactionRepository transactionRepository;

    // Lock map for each stock to ensure isolated access per stock name
    private final ConcurrentHashMap<String, Lock> stockLocks = new ConcurrentHashMap<>();

    private Lock getLockForStock(String stockName) {
        return stockLocks.computeIfAbsent(stockName, k -> new ReentrantLock());
    }

    @Transactional
    public void matchOffersAndRequests(String stockName) {
        Lock lock = getLockForStock(stockName);
        lock.lock();
        try {
            List<Offer> availableOffers = offerRepository.findByStockCompanyName(stockName);
            List<Request> availableRequests = requestRepository.findByStockCompanyName(stockName);

            for (Offer offer : availableOffers) {
                Iterator<Request> requestIterator = availableRequests.iterator();

                while (requestIterator.hasNext()) {
                    Request request = requestIterator.next();
                    if (offer.getPricePerShare() <= request.getMaxPricePerShare()) {
                        processTransaction(offer, request);

                        if (offer.getIsFulfilled()) {
                            break; // Move to the next offer if fully matched
                        }

                        if (request.getIsFulfilled()) {
                            requestIterator.remove(); // Remove fully matched request
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void processTransaction(Offer offer, Request request) {
        int transactionQuantity = Math.min(offer.getQuantity(), request.getQuantity());
        double transactionPrice = offer.getPricePerShare();

        Lock lock = getLockForStock(offer.getStock().getCompanyName());
        lock.lock();
        try {

            Transaction transaction = Transaction.builder()
                    .offer(offer)
                    .request(request)
                    .quantity(transactionQuantity)
                    .pricePerShare(transactionPrice)
                    .build();

            transactionRepository.save(transaction);

            // Update offer and request quantities
            offer.setQuantity(offer.getQuantity() - transactionQuantity);
            request.setQuantity(request.getQuantity() - transactionQuantity);

            // Save the updated offer and request states
            offerRepository.save(offer);
            requestRepository.save(request);
        } finally {
            lock.unlock();
        }
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
}

