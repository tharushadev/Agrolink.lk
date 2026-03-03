package com.example.demo.agrolink.repository;

import com.example.demo.agrolink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom query to find a user by their email/username
    Optional<User> findByUsername(String username);
}