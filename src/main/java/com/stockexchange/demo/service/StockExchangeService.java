package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.RequestRepository;
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
    private final TransactionService transactionService;

    @Transactional
    public void matchOffersAndRequests(String stockName) {
        List<Offer> availableOffers = offerRepository.findByStockCompanyName(stockName);
        List<Request> availableRequests = requestRepository.findByStockCompanyName(stockName);

        for (Offer offer : availableOffers) {
            Iterator<Request> requestIterator = availableRequests.iterator();

            while (requestIterator.hasNext()) {
                Request request = requestIterator.next();

                if (offer.getPricePerShare() <= request.getMaxPricePerShare()) {
                    Transaction transaction = transactionService.createTransaction(offer, request);

                    offerRepository.save(offer);
                    requestRepository.save(request);

                    if (offer.getIsFulfilled()) {
                        break;
                    }
                }
            }
        }
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
}
