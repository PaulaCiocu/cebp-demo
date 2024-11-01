package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private final RequestRepository requestRepository;

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
