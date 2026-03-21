package com.agrolink.repository;

import com.agrolink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    // ✅ FIXED: Finds all farmers globally, and ignores uppercase/lowercase differences!
    List<User> findByRoleIgnoreCase(String role);
}