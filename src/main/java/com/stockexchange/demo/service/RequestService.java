package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public Request updateRequest(Long id, Request requestDetails) {
        return requestRepository.findById(id)
                .map(request -> {
                    request.setStock(requestDetails.getStock());
                    request.setMaxPricePerShare(requestDetails.getMaxPricePerShare());
                    request.setQuantity(requestDetails.getQuantity());
                    return requestRepository.save(request);
                })
                .orElseThrow(() -> new RuntimeException("Request not found with id " + id));
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}
