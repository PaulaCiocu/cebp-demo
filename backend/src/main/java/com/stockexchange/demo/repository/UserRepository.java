package com.stockexchange.demo.repository;

import com.stockexchange.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneById(Long id);
    Optional<User> findByEmail(String email);
}
