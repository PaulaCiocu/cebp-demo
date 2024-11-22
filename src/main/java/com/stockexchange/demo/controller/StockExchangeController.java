package com.stockexchange.demo.controller;

import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.service.StockExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-exchange")
@RequiredArgsConstructor
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return stockExchangeService.getAllTransactions();
    }

    @GetMapping("/offers")
    public List<Offer> getAllOffers() {
        return stockExchangeService.getAllOffers();
    }

    @GetMapping("/requests")
    public List<Request> getAllRequests() {
        return stockExchangeService.getAllRequests();
    }

    /*
    @PostMapping("/match/{stockName}")
    public ResponseEntity<String> matchOffersAndRequests(@PathVariable String stockName) {
        stockExchangeService.matchOffersAndRequests(stockName);
        return ResponseEntity.ok("Matching process completed for stock: " + stockName);
    }

     */
}
