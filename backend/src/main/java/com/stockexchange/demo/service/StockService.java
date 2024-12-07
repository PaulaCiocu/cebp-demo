package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.Stock.StockDto;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final OfferService offerService;
    private final RequestService requestService;


    public StockService(StockRepository stockRepository, OfferService offerService, RequestService requestService, RequestService requestService1) {
        this.stockRepository = stockRepository;
        this.offerService = offerService;
        this.requestService = requestService1;
    }

    public Stock createStock(StockDto stock) {

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

    public Stock updateStock(Long id, StockDto stockDetails) {
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

    public List<Offer> getAllOffers(Long id) {
        return offerService.getAllOffersByStockId(id);
    }

    public List<Request> getAllRequests(Long stockId) {
        return requestService.getAllRequestsByStockId(stockId);
    }
}

