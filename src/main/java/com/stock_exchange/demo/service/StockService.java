package com.stock_exchange.demo.service;

import com.stock_exchange.demo.entity.Stock;
import com.stock_exchange.demo.repository.StockJdbcRepository;
import com.stock_exchange.demo.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    //private final StockJdbcRepository stockRepository;
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
       return stockRepository.findAll();
    }

    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

}
