package com.stock_exchange.demo.controller;

import com.stock_exchange.demo.entity.Offer;
import com.stock_exchange.demo.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {
        // Delegate to the service layer
        Offer savedOffer = offerService.createOffer(offer);
        return new ResponseEntity<>(savedOffer, HttpStatus.CREATED);
    }
}
