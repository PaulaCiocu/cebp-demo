package com.stockexchange.demo.controller;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.service.OfferService;
import com.stockexchange.demo.service.RequestService;
import com.stockexchange.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class    TransactionController {

    private final TransactionService transactionService;
    private final OfferService offerService;
    private final RequestService requestService;

    @Autowired
    public TransactionController(TransactionService transactionService, OfferService offerService, RequestService requestService) {
        this.transactionService = transactionService;
        this.offerService = offerService;
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestParam Long offerId,
                                                         @RequestParam Long requestId) {
        return transactionService.createTransaction(offerId, requestId);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
