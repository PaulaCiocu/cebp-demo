package com.stock_exchange.demo.controller;

import com.stock_exchange.demo.entity.Seller;
import com.stock_exchange.demo.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller savedSeller = sellerService.saveSeller(seller);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }
}
