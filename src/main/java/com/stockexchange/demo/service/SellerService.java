package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Seller.SellerCreateDto;
import com.stockexchange.demo.dto.Seller.SellerUpdateDto;
import com.stockexchange.demo.entity.Buyer;
import com.stockexchange.demo.entity.Seller;
import com.stockexchange.demo.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Seller createSeller(SellerCreateDto seller) {

        Seller newSeller = new Seller();

        newSeller.setName(seller.getName());

        return sellerRepository.save(newSeller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Seller updateSeller(Long id, SellerUpdateDto sellerDetails) {
        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (sellerDetails.getName() != null) {
            existingSeller.setName(sellerDetails.getName());
        }

        return sellerRepository.save(existingSeller);
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}
