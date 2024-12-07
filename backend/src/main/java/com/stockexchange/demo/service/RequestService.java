package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Request.RequestDto;
import com.stockexchange.demo.entity.*;
import com.stockexchange.demo.repository.RequestRepository;
import com.stockexchange.demo.repository.StockRepository;
import com.stockexchange.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;;
    private final StockRepository stockRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, UserRepository userRepository, StockRepository stockRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    public Request createRequest(RequestDto request) {

        Request newRequest = new Request();

        User buyer = userRepository.findOneById(request.getUserId());

        Stock stock = stockRepository.findOneById(request.getStockId());

        newRequest.setQuantity(request.getQuantity());
        newRequest.setMaxPricePerShare(request.getMaxPricePerShare());
        newRequest.setUser(buyer);
        newRequest.setStock(stock);

        return requestRepository.save(newRequest);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public Request updateRequest(Long id, RequestDto requestDetails) {

        Request existingRequest = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (requestDetails.getMaxPricePerShare() != null) {
            existingRequest.setMaxPricePerShare(requestDetails.getMaxPricePerShare());
        }

        if (requestDetails.getQuantity() != null) {
            existingRequest.setQuantity(requestDetails.getQuantity());
        }

        if (requestDetails.getUserId() != null) {
            User newBuyer = userRepository.findOneById(requestDetails.getUserId());
            existingRequest.setUser(newBuyer);
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

    public Optional<List<Request>> getRequestByUserId(Long userId) {
        List<Request> requests = requestRepository.findByUserId(userId);
        return requests.isEmpty() ? Optional.empty() : Optional.of(requests);
    }

    public List<Request> getAllRequestsByStockId(Long stockId) {
        List<Request> requests = requestRepository.findByStockId(stockId);
        return requests;
    }
}
