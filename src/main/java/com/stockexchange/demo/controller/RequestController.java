package com.stockexchange.demo.controller;

import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @PostMapping
    public Request addRequest(@RequestBody Request request) {
        return requestService.addRequest(request);
    }

    @GetMapping("/buyer/{buyerId}")
    public List<Request> getRequestsByBuyer(@PathVariable Long buyerId) {
        return requestService.getRequestsByBuyer(buyerId);
    }
}
