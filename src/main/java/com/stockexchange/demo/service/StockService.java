package com.stockexchange.demo.service;

import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;


    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        return stockRepository.findById(id)
                .map(stock -> {
                    stock.setCompanyName(stockDetails.getCompanyName());
                    stock.setTotalShares(stockDetails.getTotalShares());
                    return stockRepository.save(stock);
                })
                .orElseThrow(() -> new RuntimeException("Stock not found with id " + id));
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}

