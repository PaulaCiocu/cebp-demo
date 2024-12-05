package com.stockexchange.demo.initializer;

import com.stockexchange.demo.entity.User;
import com.stockexchange.demo.repository.UserRepository;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class UserDataInitializer {

    private final UserRepository userRepository;

    public UserDataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
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
}
