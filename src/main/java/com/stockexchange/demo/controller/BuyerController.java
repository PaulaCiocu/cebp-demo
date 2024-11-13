package com.stockexchange.demo.controller;

import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/buyers")
public class BuyerController {

    private final BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @PostMapping
    public Buyer createBuyer(@RequestBody Buyer buyer) {
        return buyerService.createBuyer(buyer);
    }

    @GetMapping
    public List<Buyer> getAllBuyers() {
        return buyerService.getAllBuyers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Long id) {
        return buyerService.getBuyerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long id, @RequestBody Buyer buyerDetails) {
        try {
            Buyer updatedBuyer = buyerService.updateBuyer(id, buyerDetails);
            return ResponseEntity.ok(updatedBuyer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
