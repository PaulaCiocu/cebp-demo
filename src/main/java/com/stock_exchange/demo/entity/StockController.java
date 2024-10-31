package com.stock_exchange.demo.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping()
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping
    public ResponseEntity<Void> addStock(@RequestBody Stock stock) {
        stockService.addStock(stock.getCompanyName(), stock.getTotalShares());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
