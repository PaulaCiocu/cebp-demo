package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Stock.StockCreateDto;
import com.stockexchange.demo.dto.Stock.StockUpdateDto;
import com.stockexchange.demo.entity.Seller;
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

    public Stock createStock(StockCreateDto stock) {

        Stock newStock = new Stock();

        newStock.setCompanyName(stock.getCompanyName());
        newStock.setTotalShares(stock.getTotalShares());

        return stockRepository.save(newStock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock updateStock(Long id, StockUpdateDto stockDetails) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (stockDetails.getCompanyName() != null) {
            existingStock.setCompanyName(stockDetails.getCompanyName());
        }

        if (stockDetails.getTotalShares() != null) {
            existingStock.setTotalShares(stockDetails.getTotalShares());
        }

        return stockRepository.save(existingStock);
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}

