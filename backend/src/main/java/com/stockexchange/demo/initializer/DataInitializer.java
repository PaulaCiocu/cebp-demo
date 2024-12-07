package com.stockexchange.demo.initializer;

import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Stock;
import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.repository.OfferRepository;
import com.stockexchange.demo.repository.StockRepository;
import com.stockexchange.demo.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.text.DecimalFormat;
import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataInitializer {

    private final OfferRepository offerRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    public DataInitializer(OfferRepository offerRepository, StockRepository stockRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        initStocks(); // Ensure stocks are initialized before creating offers
        initUsers();
        initOffers(); // Now create offers with available stock data
    }

    public void initUsers() {
        // Check if the database is already populated
        if (userRepository.count() == 0) {
            List<User> users = Arrays.asList(
                    User.builder().name("John Doe").email("john.doe@example.com").password("password123").build(),
                    User.builder().name("Jane Smith").email("jane.smith@example.com").password("securepass").build(),
                    User.builder().name("Alice Johnson").email("alice.johnson@example.com").password("alicepass").build(),
                    User.builder().name("Bob Brown").email("bob.brown@example.com").password("bobbypass").build(),
                    User.builder().name("Charlie Green").email("charlie.green@example.com").password("charliepass").build(),
                    User.builder().name("Diana White").email("diana.white@example.com").password("dianapass").build(),
                    User.builder().name("Edward Black").email("edward.black@example.com").password("edwardpass").build(),
                    User.builder().name("Fiona Grey").email("fiona.grey@example.com").password("fionapass").build(),
                    User.builder().name("George Blue").email("george.blue@example.com").password("georgepass").build(),
                    User.builder().name("Hannah Gold").email("hannah.gold@example.com").password("hannahpass").build(),
                    User.builder().name("Ian Silver").email("ian.silver@example.com").password("ianpass").build(),
                    User.builder().name("Julia Violet").email("julia.violet@example.com").password("juliapass").build(),
                    User.builder().name("Kevin Amber").email("kevin.amber@example.com").password("kevinpass").build(),
                    User.builder().name("Laura Crimson").email("laura.crimson@example.com").password("laurapass").build(),
                    User.builder().name("Michael Cyan").email("michael.cyan@example.com").password("michaelpass").build()
            );
            userRepository.saveAll(users);
        }
    }

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

    private void initOffers() {
        // Check if the database is already populated
        if (offerRepository.count() == 0) {
            List<Stock> stocks = stockRepository.findAll(); // Fetch all stocks from the repository
            List<User> users = userRepository.findAll();   // Fetch all users from the repository

            DecimalFormat df = new DecimalFormat("#.00");

            List<Offer> offers = IntStream.range(0, 30)
                    .mapToObj(i -> {
                        Stock stock = stocks.get(i % stocks.size()); // Cycle through stocks
                        User user = users.get(i % users.size());   // Cycle through users
                        int quantity = new Random().nextInt(100) + 1; // Random quantity between 1 and 100
                        double pricePerShare = new Random().nextDouble() * 300; // Random price per share between 0 and 300
                        String formattedPrice = df.format(pricePerShare); // Format to 2 decimal places
                        return Offer.builder()
                                .quantity(quantity)
                                .pricePerShare(Double.parseDouble(formattedPrice)) // Convert the formatted string back to double
                                .stock(stock)
                                .user(user)
                                .isFulfilled(false)
                                .build();
                    })
                    .collect(Collectors.toList());

            offerRepository.saveAll(offers);
        }
    }
}
