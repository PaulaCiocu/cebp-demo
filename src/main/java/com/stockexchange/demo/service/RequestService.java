package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Request.RequestCreateDto;
import com.stockexchange.demo.dto.Request.RequestUpdateDto;
import com.stockexchange.demo.entity.*;
import com.stockexchange.demo.repository.BuyerRepository;
import com.stockexchange.demo.repository.RequestRepository;
import com.stockexchange.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final BuyerRepository buyerRepository;;
    private final StockRepository stockRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, BuyerRepository buyerRepository, StockRepository stockRepository) {
        this.requestRepository = requestRepository;
        this.buyerRepository = buyerRepository;
        this.stockRepository = stockRepository;
    }

    public Request createRequest(RequestCreateDto request) {

        Request newRequest = new Request();

        Buyer buyer = buyerRepository.findOneById(request.getBuyerId());

        Stock stock = stockRepository.findOneById(request.getStockId());

        newRequest.setQuantity(request.getQuantity());
        newRequest.setMaxPricePerShare(request.getMaxPricePerShare());
        newRequest.setBuyer(buyer);
        newRequest.setStock(stock);

        return requestRepository.save(newRequest);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public Request updateRequest(Long id, RequestUpdateDto requestDetails) {

        Request existingRequest = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (requestDetails.getMaxPricePerShare() != null) {
            existingRequest.setMaxPricePerShare(requestDetails.getMaxPricePerShare());
        }

        if (requestDetails.getQuantity() != null) {
            existingRequest.setQuantity(requestDetails.getQuantity());
        }

        if (requestDetails.getBuyerId() != null) {
            Buyer newBuyer = buyerRepository.findOneById(requestDetails.getBuyerId());
            existingRequest.setBuyer(newBuyer);
        }

        if (requestDetails.getStockId() != null) {
            Stock newStock = stockRepository.findOneById(requestDetails.getStockId());
            existingRequest.setStock(newStock);
        }


        return requestRepository.save(existingRequest);
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}
