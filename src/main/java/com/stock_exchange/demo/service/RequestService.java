package com.stock_exchange.demo.service;

import com.stock_exchange.demo.entity.Request;
import com.stock_exchange.demo.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> getRequestsByBuyer(Long buyerId) {
        return requestRepository.findByBuyerId(buyerId);
    }
}
