package com.stockexchange.demo.controller;

import com.stockexchange.demo.dto.User.LoginRequestDto;
import com.stockexchange.demo.dto.User.UserCreateDto;
import com.stockexchange.demo.entity.Offer;
import com.stockexchange.demo.entity.Request;
import com.stockexchange.demo.entity.Transaction;
import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Set;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody UserCreateDto user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.authenticate(loginRequestDto);
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserCreateDto userDetails) {
        try {
            User updateUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updateUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/offers")
    public ResponseEntity<List<Offer>> getUserOffers(@PathVariable Long userId) {
        return userService.getOffersByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<Request>> getUserRequests(@PathVariable Long userId) {
        return userService.getRequestsByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/transactions")
    public Set<Transaction> getUserTransactions(@PathVariable Long userId) {
        return  userService.getTransactionsByUserId(userId);

    }
}
