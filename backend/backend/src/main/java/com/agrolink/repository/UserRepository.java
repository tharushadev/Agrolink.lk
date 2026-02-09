package com.agrolink.repository;

import com.agrolink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // ✅ Find by Phone Number
    Optional<User> findByPhoneNumber(String phoneNumber);
}