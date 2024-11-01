package com.stock_exchange.demo.controller;

import com.stock_exchange.demo.entity.Offer;
import com.stock_exchange.demo.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @PostMapping
    public Offer addOffer(@RequestBody Offer offer) {
        return offerService.addOffer(offer);
    }

    @GetMapping("/seller/{sellerId}")
    public List<Offer> getOffersBySeller(@PathVariable String sellerId) {
        return offerService.getOffersBySeller(sellerId);
    }
}
