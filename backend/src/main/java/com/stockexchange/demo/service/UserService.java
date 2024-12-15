package com.stockexchange.demo.service;
import com.stockexchange.demo.dto.User.LoginRequestDto;
import com.stockexchange.demo.dto.User.LoginResponseDto;
import com.stockexchange.demo.dto.User.UserCreateDto;
import com.stockexchange.demo.dto.User.UserUpdateDto;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
        // Check if email already exists in the database
        if (userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setBalance(15000.00);
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

    public User updateUser(Long id, UserUpdateDto userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        if (userDetails.getName() != null) {
            existingUser.setName(userDetails.getName());
            existingUser.setBalance(userDetails.getBalance());
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

    public ResponseEntity<LoginResponseDto> authenticate(LoginRequestDto loginRequestDto) {
        // Find user by email
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElse(null);

        // Check if user exists and password matches
        if (user == null || !loginRequestDto.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(new LoginResponseDto("Invalid credentials", null), HttpStatus.UNAUTHORIZED);
        }

        // Return success message and userId
        LoginResponseDto responseDto = new LoginResponseDto("Authentication successful", user.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }





}
