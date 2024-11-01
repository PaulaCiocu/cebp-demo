package com.stockexchange.demo.controller;

import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    // Add a new seller with duplicate check
    @PostMapping
    public ResponseEntity<?> addSeller(@RequestBody Seller seller) {
        Seller createdSeller = sellerService.addSeller(seller);
        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
    }

    // Retrieve all sellers
    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    // Retrieve a specific seller by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSellerById(@PathVariable Long id) {
        try {
            Seller seller = sellerService.getSellerById(id);
            return new ResponseEntity<>(seller, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get all offers by a specific seller
    @GetMapping("/{sellerId}/offers")
    public ResponseEntity<?> getOffersBySeller(@PathVariable Long sellerId) {
        try {
            List<Offer> offers = sellerService.getOffersBySeller(sellerId);
            return new ResponseEntity<>(offers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
