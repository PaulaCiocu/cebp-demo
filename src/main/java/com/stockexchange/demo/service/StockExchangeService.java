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

@Service
@RequiredArgsConstructor
public class StockExchangeService {

    private final OfferRepository offerRepository;
    private final RequestRepository requestRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Match offers and requests for a specific stock.
     * Uses @Transactional to ensure database consistency.
     */
    @Transactional
    public void matchOffersAndRequests(String stockName) {
        // Fetch available offers and requests for the stock
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
    }

    /**
     * Process a transaction between an offer and a request.
     * Calculates transaction details, updates the offer and request, and saves the transaction.
     */
    private void processTransaction(Offer offer, Request request) {
        int transactionQuantity = Math.min(offer.getQuantity(), request.getQuantity());
        double transactionPrice = offer.getPricePerShare();

        // Create and save the transaction
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

        // Mark as fulfilled if quantity reaches zero
        if (offer.getQuantity() == 0) {
            offer.setIsFulfilled(true);
        }
        if (request.getQuantity() == 0) {
            request.setIsFulfilled(true);
        }

        // Save the updated offer and request
        offerRepository.save(offer);
        requestRepository.save(request);
    }

    /**
     * Retrieve all transactions.
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Retrieve all offers.
     */
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    /**
     * Retrieve all requests.
     */
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
}