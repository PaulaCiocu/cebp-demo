package com.stockexchange.demo.initializer;

import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StockDataInitializer {


    private StockRepository stockRepository;
    public StockDataInitializer(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void initStocks() {
        // Check if the database is already populated
        if (stockRepository.count() == 0) {
            List<Stock> stocks = Arrays.asList(
                    new Stock("Apple Inc.", 1000),
                    new Stock("Microsoft Corp.", 1500),
                    new Stock("Google LLC", 2000),
                    new Stock("Amazon.com Inc.", 1200),
                    new Stock("Tesla Inc.", 800),
                    new Stock("Meta Platforms", 1300),
                    new Stock("NVIDIA Corp.", 900),
                    new Stock("Samsung Electronics", 1400),
                    new Stock("Intel Corp.", 1700),
                    new Stock("Cisco Systems", 1100),
                    new Stock("IBM Corp.", 1000),
                    new Stock("Sony Group", 900),
                    new Stock("Oracle Corp.", 950),
                    new Stock("Twitter Inc.", 1100),
                    new Stock("Zoom Video", 750),
                    new Stock("Qualcomm Inc.", 1250),
                    new Stock("Netflix Inc.", 800),
                    new Stock("Adobe Inc.", 1300),
                    new Stock("Dell Technologies", 1000),
                    new Stock("HP Inc.", 950),
                    new Stock("Spotify Tech.", 700),
                    new Stock("Salesforce Inc.", 1200),
                    new Stock("Dropbox Inc.", 600),
                    new Stock("eBay Inc.", 1100),
                    new Stock("PayPal Holdings", 1300),
                    new Stock("Uber Technologies", 800),
                    new Stock("Lyft Inc.", 700),
                    new Stock("Pinterest Inc.", 900),
                    new Stock("Snap Inc.", 850),
                    new Stock("Shopify Inc.", 1000)
            );
            stockRepository.saveAll(stocks);
        }
    }
}
