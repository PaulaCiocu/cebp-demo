package com.stock_exchange.demo.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private final StockJdbcRepository stockRepository;

    @Autowired
    public StockService(StockJdbcRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
       return stockRepository.findAll();
    }

    public void addStock(String companyName, int totalShares) {
        Stock stock = new Stock(companyName, totalShares);
        stockRepository.save(stock);
    }
}
