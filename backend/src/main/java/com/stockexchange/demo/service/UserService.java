package com.stockexchange.demo.service;

import com.stockexchange.demo.dto.User.UserCreateDto;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OfferService offerService;
    private final RequestService requestService;
    private final TransactionService transactionService;

    public UserService(UserRepository userRepository, OfferService offerService, RequestService requestService, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.offerService = offerService;
        this.requestService = requestService;
        this.transactionService = transactionService;
    }

    public User createUser(UserCreateDto userCreateDto) {
        User newUser = new User();
        newUser.setEmail(userCreateDto.getEmail());
        newUser.setName(userCreateDto.getName());
        newUser.setPassword(userCreateDto.getPassword());

       return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, UserCreateDto userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (userDetails.getName() != null) {
            existingUser.setName(userDetails.getName());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<List<Offer>> getOffersByUserId(Long id) {
        return offerService.getOfferByUserId(id);

    }

    public Optional<List<Request>> getRequestsByUserId(Long id) {
        return requestService.getRequestByUserId(id);
    }

    public Set<Transaction> getTransactionsByUserId(Long id) {
        return transactionService.getTransactionByUserId(id);
    }


}
